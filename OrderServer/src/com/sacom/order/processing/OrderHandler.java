package com.sacom.order.processing;

import com.sacom.order.common.OrderDescription;
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

public class OrderHandler implements Runnable {
  private OrderDescription orderDescription;
  private DocumentBuilderFactory documentBuilderFactory;

  private HashMap<String, Document> outgoingOrdersMap;

  OrderHandler(OrderDescription _orderDescription, DocumentBuilderFactory _documentBuilderFactory) {
    orderDescription = _orderDescription;
    documentBuilderFactory = _documentBuilderFactory;

    outgoingOrdersMap = new HashMap<>();
  }

  @Override
  public void run() {
    File file = readInputFile();
    if (file != null) {
      DocumentBuilder documentBuilder = null;
      try {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        // TODO: documentBuilder.setErrorHandler();
      } catch (ParserConfigurationException _ex) {
        // TODO:
        //System.err.println("ERROR: initializing or parsing XML order: " + _ex.toString());
        _ex.printStackTrace();
      }

      Document xmlDocument = null;
      if (documentBuilder != null) {
        try {
          xmlDocument = documentBuilder.parse(file);
          //xmlDocument.normalizeDocument();
          xmlDocument.normalize();
        } catch (SAXException e) {
          // TODO:
          e.printStackTrace();
        } catch (IOException e) {
          // TODO:
          e.printStackTrace();
        }
      }

      if (xmlDocument != null) {
        Element rootElem = xmlDocument.getDocumentElement();
        NamedNodeMap attributeMap = rootElem.getAttributes();
        for (int i = 0; i < attributeMap.getLength(); i++) {
          Node n = attributeMap.item(i);
          System.out.println(n.getNodeName());
        }

        NodeList orderNodes = rootElem.getElementsByTagName("order");
        for (int i = 0; i < orderNodes.getLength(); i++) {
          final Node orderNode = orderNodes.item(i);
          handleOrderNode(orderNode);
        }
      }

      for(final String supplierName : outgoingOrdersMap.keySet()) {
        System.out.println("-----");
        Document outOrderXml = outgoingOrdersMap.get(supplierName);
        System.out.println(transformNodeToString(outOrderXml));
      }

    } else {
      System.err.println("Cannot open input document.");
    }
  }

  private File readInputFile() {
    FileSystem fs = FileSystems.getDefault();
    String directoryAbsoutePath = orderDescription.getDirectoryPath().toString();
    String fileName = orderDescription.getFileName().toString();
    Path fileFullPathAndFileName = fs.getPath(directoryAbsoutePath, fileName);
    File file = new File(fileFullPathAndFileName.toUri());
    return file;
  }

  private void handleOrderNode(final Node _orderNode) {
    final NamedNodeMap orderAttributes = _orderNode.getAttributes();
    final Node orderIdNode = orderAttributes.getNamedItem("ID");
    final String orderIdString = orderIdNode.getNodeValue();
    assert _orderNode instanceof Element;
    final Element orderElement = (Element) _orderNode;
    final NodeList productNodes = orderElement.getElementsByTagName("product");
    for (int i = 0; i < productNodes.getLength(); i++) {
      final Node productNode = productNodes.item(i);
      handleProductNode(productNode, orderIdString);
    }
  }

  private String extractSupplierName(final Node _productNode) {
    assert _productNode instanceof Element;
    final Element productElement = (Element) _productNode;
    final NodeList supplierNodes = productElement.getElementsByTagName("supplier");
    if (supplierNodes.getLength() != 1) {
      // TODO: handle error
    }
    final Node supplierNode = supplierNodes.item(0);
    final String supplier = supplierNode.getTextContent();
    return supplier;
  }

  private void removeSupplierNode(final Node _productNode) {
    assert _productNode instanceof Element;
    final Element productElement = (Element) _productNode;
    final NodeList supplierNodes = productElement.getElementsByTagName("supplier");
    if (supplierNodes.getLength() != 1) {
      // TODO: handle error
    }
    final Node supplierNode = supplierNodes.item(0);
    _productNode.removeChild(supplierNode);
  }

  private void handleProductNode(final Node _productNode, final String orderId) {
    // extract supplier
    //
    final String supplierName = extractSupplierName(_productNode);

    // obtain a destination xml document
    //
    final Document outXmlDocument = getOrCreateOutgingOrder(supplierName);
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

//    System.out.println("-----");
//    System.out.println(transformNodeToString(importedProductNode));
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
    String xml = writer.toString();
    return xml;
  }

  private Document getOrCreateOutgingOrder(final String _supplier) {
    if (!outgoingOrdersMap.containsKey(_supplier)) {
      DocumentBuilder documentBuilder = null;
      try {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
      } catch (ParserConfigurationException _ex) {
        //System.err.println("ERROR: initializing or parsing XML order: " + _ex.toString());
        // TODO:
        _ex.printStackTrace();
      }

      if( documentBuilder != null ) {
        Document newDocument = documentBuilder.newDocument();
        Element rootElement = newDocument.createElement("products");
        newDocument.appendChild(rootElement);

        outgoingOrdersMap.put(_supplier, newDocument);
      }
    }

    return outgoingOrdersMap.get(_supplier);
  }
}

