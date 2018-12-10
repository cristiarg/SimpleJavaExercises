package com.sacom.order.processing;

import com.sacom.order.common.OrderDescription;
import com.sacom.order.common.OrderDispatcher;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

public class OrderHandler implements Runnable {
  private OrderDispatcher dispatcher;

  private OrderDescription orderDescription;
  private DocumentBuilderFactory documentBuilderFactory;

  private HashMap<String, Document> outgoingOrdersMap;

  OrderHandler(OrderDispatcher _dispatcher, OrderDescription _orderDescription, DocumentBuilderFactory _documentBuilderFactory) {
    dispatcher = _dispatcher;
    orderDescription = _orderDescription;
    documentBuilderFactory = _documentBuilderFactory;

    outgoingOrdersMap = new HashMap<>();
  }

  @Override
  public void run() {
    File file = readInputFile();
    if (file != null) {
      final Document xmlDocument = parseXmlDocument(file);
      if (xmlDocument != null) {
        processXmlDocument(xmlDocument);
      }

      for (final String supplierName : outgoingOrdersMap.keySet()) {
        final Document outOrderXml = outgoingOrdersMap.get(supplierName);
        final String outFileName = constructOutputFileName(supplierName);
        //System.out.println("----- " + outFileName);
        //System.out.println(transformNodeToString(outOrderXml));
        try {
          final OrderDescription orderDescription = new OrderDescription("processing",
              "fileName", outFileName,
              "xmlDocument", outOrderXml);
          dispatcher.dispatch(orderDescription);
        } catch (Exception _ex) {
          System.err.println("ERROR: order cannot be instantiated: " + _ex.toString());
        }
      }

    } else {
      System.err.println("Cannot open input document.");
    }
  }

  private DocumentBuilder getDocumentBuilder() {
    DocumentBuilder documentBuilder = null;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      // TODO:
      documentBuilder.setErrorHandler(null);
    } catch (ParserConfigurationException _ex) {
      // nop
    }
    return documentBuilder;
  }

  private Document parseXmlDocument(File _file) {
    DocumentBuilder documentBuilder = getDocumentBuilder();
    if (documentBuilder != null) {
      try {
        // validation
        final boolean b = documentBuilder.isValidating();
        final Schema s = documentBuilder.getSchema();
        final Validator v = s.newValidator();
        v.validate(new StreamSource(_file));

        // parsing
        Document xmlDocument = documentBuilder.parse(_file);
        xmlDocument.normalize();
        return  xmlDocument;

      } catch (SAXException | IOException _ex) {
        System.err.println("ERROR: validating & parsing input XML document: " + _ex.toString());
        // TODO: after an XML is not validated, apparently it is not possible to delete it until the application is closed
      }
    }
    return null;
  }

  private File readInputFile() {
    final FileSystem fs = FileSystems.getDefault();
    final Path directoryAsPath = (Path) orderDescription.item("directory");
    final Path fileAsPath = (Path) orderDescription.item("file");
    final String directoryAbsolutePath = directoryAsPath.toString();
    final String fileName = fileAsPath.toString();
    final Path fileFullPathAndFileName = fs.getPath(directoryAbsolutePath, fileName);
    File f = new File(fileFullPathAndFileName.toUri());
    if (f.exists()) {
      return f;
    } else {
      return null;
    }
  }

  private String constructOutputFileName(String _supplierNode) {
    StringBuilder sb = new StringBuilder(_supplierNode);
    final String orderNumber = (String)orderDescription.item("orderNumber");
    if (orderNumber == null) {
      System.err.println("ERROR: 'orderNumber' expected in order description");
    }
    sb.append(orderNumber);
    sb.append(".xml");
    return sb.toString();
  }

  private void processXmlDocument(Document _xmlDocument) {
    Element rootElem = _xmlDocument.getDocumentElement();
    NodeList orderNodes = rootElem.getElementsByTagName("order");
    for (int i = 0; i < orderNodes.getLength(); i++) {
      final Node orderNode = orderNodes.item(i);
      processOrderNode(orderNode);
    }
  }

  private void processOrderNode(final Node _orderNode) {
    final NamedNodeMap orderAttributes = _orderNode.getAttributes();
    final Node orderIdNode = orderAttributes.getNamedItem("ID");
    final String orderIdString = orderIdNode.getNodeValue();
    assert _orderNode instanceof Element;
    final Element orderElement = (Element) _orderNode;
    final NodeList productNodes = orderElement.getElementsByTagName("product");
    for (int i = 0; i < productNodes.getLength(); i++) {
      final Node productNode = productNodes.item(i);
      processProductNode(productNode, orderIdString);
    }
  }

  private String extractSupplierName(final Node _productNode) {
    assert _productNode instanceof Element;
    final Element productElement = (Element) _productNode;
    final NodeList supplierNodes = productElement.getElementsByTagName("supplier");
    if (supplierNodes.getLength() != 1) {
      System.err.println("ERROR: more than one 'supplier' nodes found");
    }
    final Node supplierNode = supplierNodes.item(0);
    return supplierNode.getTextContent();
  }

  private void removeSupplierNode(final Node _productNode) {
    assert _productNode instanceof Element;
    final Element productElement = (Element) _productNode;
    final NodeList supplierNodes = productElement.getElementsByTagName("supplier");
    if (supplierNodes.getLength() != 1) {
      System.err.println("ERROR: more than one 'supplier' nodes found");
    }
    final Node supplierNode = supplierNodes.item(0);
    _productNode.removeChild(supplierNode);
  }

  private void processProductNode(final Node _productNode, final String orderId) {
    // extract supplier
    //
    final String supplierName = extractSupplierName(_productNode);

    // obtain a destination xml document
    //
    final Document outXmlDocument = getOrCreateOutgoingOrder(supplierName);
    final Element outRootElement = outXmlDocument.getDocumentElement();

    // import the product node into the new document
    //
    final Node importedProductNode = outXmlDocument.importNode(_productNode, true);
    outRootElement.appendChild(importedProductNode);

    // remove/add [un]needed elements
    //
    removeSupplierNode(importedProductNode);
    importedProductNode.normalize();
    final Element newOrderIdNode = outXmlDocument.createElement("orderid");
    newOrderIdNode.setTextContent(orderId);
    importedProductNode.appendChild(newOrderIdNode);
    newOrderIdNode.normalize();
  }

  private String transformNodeToString(final Node _node) {
    StringWriter writer = new StringWriter();
    Transformer transformer = null;
    try {
      transformer = TransformerFactory.newInstance().newTransformer();
    } catch (TransformerConfigurationException _ex) {
      System.err.println("ERROR: transformer factory - new transformer: " + _ex.toString());
      _ex.printStackTrace();
    }
    try {
      transformer.transform(new DOMSource(_node), new StreamResult(writer));
    } catch (TransformerException _ex) {
      System.err.println("ERROR: transformer - transform: " + _ex.toString());
      _ex.printStackTrace();
    }
    return writer.toString();
  }

  private Document getOrCreateOutgoingOrder(final String _supplier) {
    if (!outgoingOrdersMap.containsKey(_supplier)) {
      DocumentBuilder documentBuilder = null;
      try {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
      } catch (ParserConfigurationException _ex) {
        System.err.println("ERROR: cannot construct document builder: " + _ex.toString());
      }

      if (documentBuilder != null) {
        Document newDocument = documentBuilder.newDocument();
        Element rootElement = newDocument.createElement("products");
        newDocument.appendChild(rootElement);

        outgoingOrdersMap.put(_supplier, newDocument);
      }
    }

    return outgoingOrdersMap.get(_supplier);
  }
}

