package com.sacom.order.processing;

import com.sacom.order.common.OrderDescription;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class OrderHandlerTest {

  @Test
  public void run1() {
    final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setIgnoringComments(true);
    //documentBuilderFactory.setValidating(true);

    Path dirPath = Paths.get("C:\\_test");
    Path fileName = Paths.get("order01.xml");
    try {
      final OrderDescription orderDescription = new OrderDescription("receiver",
          "directory", dirPath, "file", fileName);
      OrderHandler oh = new OrderHandler(null, orderDescription, true, documentBuilderFactory);
      oh.run();
    } catch (Exception _ex) {
      // nop
    }
  }
}