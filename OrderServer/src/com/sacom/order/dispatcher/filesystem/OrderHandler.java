package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.common.OrderDescription;
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.*;

class OrderHandler implements Runnable {
  private DispatcherSettings settings;
  private OrderDescription orderDescription;

  OrderHandler(DispatcherSettings _settings, OrderDescription _orderDescription) {
    settings = _settings;
    orderDescription = _orderDescription;
  }

  @Override
  public void run() {
    final Object fileNameAsObject = orderDescription.item("fileName");
    final Object xmlDocumentAsObject = orderDescription.item("xmlDocument");
    final boolean fileOk = fileNameAsObject instanceof String;
    final boolean xmlDocumentOk = xmlDocumentAsObject instanceof Document;
    if(fileOk && xmlDocumentOk) {
      String fileName = (String)fileNameAsObject;
      Document xmlDocument = (Document)xmlDocumentAsObject;

      FileSystem fs = FileSystems.getDefault();
      Path newAbsolutePathAndName = fs.getPath(settings.getDirectory(), fileName);
      File newFile = new File(newAbsolutePathAndName.toString());

      writeXmlDocumentToFileSystem(xmlDocument, newFile);
    } else {
      // TODO:
    }
  }

  private void writeXmlDocumentToFileSystem(Document _xmlDocument, File _file) {
    final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    Transformer transformer = null;
    try {
      transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    } catch (TransformerConfigurationException e) {
      //  TODO:
      e.printStackTrace();
    }

    if (transformer != null) {
      final DOMSource source = new DOMSource(_xmlDocument);
      final StreamResult result = new StreamResult(_file);

      try {
        transformer.transform(source, result);
      } catch (TransformerException e) {
        // TODO:
        e.printStackTrace();
      }
    }
  }

  private void debugHandling() {
//    FileSystem fs = FileSystems.getDefault();
//
//    final Path directoryAsPath = (Path)orderDescription.item("directory");
//    final Path fileAsPath = (Path)orderDescription.item("file");
//    final String oldDirectoryAbsolutePath = directoryAsPath.toString();
//    final String fileName = fileAsPath.toString();
//
//    Path oldFileAbsolutePathAndName = fs.getPath(oldDirectoryAbsolutePath, fileName);
//    Path newFileAbsolutePathAndName = fs.getPath(settings.getDirectory(), fileName);
//
//    // large files take some time to finish copying; therefore, give it a few more tries
//    int tryCount = 1 + settings.getTryCount();
//    while (tryCount > 0) {
//      try {
//        Files.move(oldFileAbsolutePathAndName, newFileAbsolutePathAndName);
//        tryCount = 0;
//      } catch (IOException _exIO) {
//        --tryCount;
//        if (tryCount == 0) {
//          // TODO: more appropriate exception handling
//          System.err.println("WARNING: moving file '" + fileName + "' failed; will try again: " + _exIO.toString());
//        } else {
//          // give it some time before trying again
//          try {
//            Thread.sleep(50);
//          } catch (InterruptedException _exTh) {
//            // TODO: better
//          }
//        }
//      }
//    }
  }
}


