package com.sacom.order.processing;

import com.sacom.order.common.OrderDescription;
import com.sacom.order.model.IncomingOrderDescription;
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
    final OrderDescription orderDescription = new IncomingOrderDescription(dirPath, fileName);

    OrderHandler oh = new OrderHandler(orderDescription, documentBuilderFactory);
    oh.run();

  }
}