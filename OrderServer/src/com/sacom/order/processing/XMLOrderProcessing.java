package com.sacom.order.processing;

import com.sacom.order.common.LifeCycle;
import com.sacom.order.common.LifeCycleException;
import com.sacom.order.common.OrderDescription;
import com.sacom.order.common.OrderDispatcher;

//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class XMLOrderProcessing implements LifeCycle, OrderDispatcher {
  private ExecutorService executor;

  private DocumentBuilderFactory documentBuilderFactory;

  @Override
  public synchronized void start() throws LifeCycleException {
    startExecutor();
    initializeXMLObects();
  }

  private void startExecutor() {
    if (executor == null) {
      executor = Executors.newWorkStealingPool();
    }
  }

  private void initializeXMLObects() throws  LifeCycleException{
    if (documentBuilderFactory == null) {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // TODO: documentBuilderFactory.setSchema();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setValidating(true);
    }
  }

  @Override
  public synchronized void stop() throws LifeCycleException {
    if (executor != null) {
      try {
        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        executor.shutdown();
        if (executor.isShutdown()) {
          executor = null;
        } else {
          throw new LifeCycleException("XML Order Processing: cannot shutdown");
        }
      } catch (InterruptedException _ex) {
        throw new LifeCycleException("XML Order Processing: termination failed", _ex);
      }
    }
  }

  @Override
  public synchronized void dispatch(OrderDescription _orderDescription) {
    if (executor != null) {
      // TODO: not interested in the result, for now
      // TODO: get the future instance, add it to a waiting queue, dispatch another thread to
      // monitor the queue
      executor.submit(new OrderHandler(_orderDescription, documentBuilderFactory));
    }
  }
}
