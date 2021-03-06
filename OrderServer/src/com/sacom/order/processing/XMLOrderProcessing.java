package com.sacom.order.processing;

import com.sacom.order.common.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class XMLOrderProcessing implements LifeCycle, MessageBrokerClient {
  private ProcessingSettings settings;

  private MessageDispatcher messageDispatcher;

  private ExecutorService executor;

  private DocumentBuilderFactory documentBuilderFactory;

  public XMLOrderProcessing(final ProcessingSettings _processingSettings) {
    settings = _processingSettings;
  }

  @Override
  public synchronized void start() throws LifeCycleException {
    logStatusBeforeStart();

    startExecutor();
    initializeXMLFactory();

    logStatusAfterStart();
  }

  private void startExecutor() {
    if (executor == null) {
      executor = Executors.newWorkStealingPool();
    }
  }

  private void initializeXMLFactory() throws  LifeCycleException{
    if (documentBuilderFactory == null) {
      documentBuilderFactory = DocumentBuilderFactory.newInstance();

      File file = new File(settings.getXsdSchemaFileName()); // "orders.xsd"
      if(!file.exists()) {
        final String absolutePath = file.getAbsolutePath();
        System.err.println("ERROR: schema file '" + settings.getXsdSchemaFileName() + "' was not found (" +  absolutePath + ")");
        file = null;
      }

      if (file != null) {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
          schema = schemaFactory.newSchema(file);
        } catch (SAXException e) {
          e.printStackTrace();
        }

        if (schema != null) {
          documentBuilderFactory.setSchema(schema);
          documentBuilderFactory.setValidating(true);
        }
      }

      documentBuilderFactory.setIgnoringComments(true);

      documentBuilderFactory.setIgnoringElementContentWhitespace(true);
    }
  }

  @Override
  public synchronized void stop() throws LifeCycleException {
    if (executor != null) {
      logStatusBeforeStop();

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

      logStatusAfterStop();
    }
  }

  private void logStatusBeforeStart() {
    System.out.println("INFO: XML Order Processing:");
    System.out.println("      starting..");
  }

  private void logStatusAfterStart() {
    System.out.println("INFO: XML Order Processing:");
    System.out.println("      started");
  }

  private void logStatusBeforeStop() {
    System.out.println("INFO: XML Order Processing:");
    System.out.println("      stopping..");
  }

  private void logStatusAfterStop() {
    System.out.println("INFO: XML Order Processing:");
    System.out.println("      stopped");
  }

  @Override
  public synchronized void dispatch(final String _interest, final MessageDescription _messageDescription) {
    if (executor != null) {
      final String nature = _messageDescription.getNature();
      if (nature.equals("receiver")) {
        // TODO: not interested in the result, for now
        // TODO: get the future instance, add it to a waiting queue, dispatch another thread to
        // monitor the queue
        executor.submit(new OrderHandler(messageDispatcher, _messageDescription, settings.isCleanUpProcessedOrderFiles(), documentBuilderFactory));
      } else {
        // TODO: not the order description we've been waiting for
        System.err.println("ERROR: Processing: unexpected nature; found '" + nature + "'; expected 'receiver'; message will be discarded");
      }
    }
  }

  @Override
  public void register(MessageBrokerServer _messageBrokerServer) {
    // keep a reference to the broker to be able to dispatch messages to it (one way or another)
    messageDispatcher = _messageBrokerServer;
    // registering ourselves
    _messageBrokerServer.subscribe("receiver", this);
  }
}
