package com.sacom.order;

import com.sacom.order.common.LifeCycleException;
import com.sacom.order.dispatcher.DispatcherException;
import com.sacom.order.dispatcher.filesystem.Dispatcher;
import com.sacom.order.dispatcher.filesystem.DispatcherSettings;
import com.sacom.order.processing.XMLOrderProcessing;
import com.sacom.order.receiver.ReceiverException;
import com.sacom.order.receiver.filesystem.Receiver;
import com.sacom.order.common.LifeCycle;
import com.sacom.order.receiver.filesystem.ReceiverSettings;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    //
    // construct settings
    //
    DispatcherSettings dispatcherSettings = null;
    try {
      dispatcherSettings = new DispatcherSettings("C:\\_o", 10);
    } catch (DispatcherException _ex) {
      System.err.println("ERROR: cannot instantiate file system dispatcher settings: " + _ex.toString());
      dispatcherSettings = null;
    }

    ReceiverSettings receiverSettings = null;
    try {
      receiverSettings = new ReceiverSettings(
          "C:\\_i", 100, TimeUnit.MILLISECONDS);
    } catch (ReceiverException _ex) {
      System.err.println("ERROR: cannot instantiate file system receiver: " + _ex.toString());
      receiverSettings = null;
    }

    //
    // construct the pipeline backwards
    //
    Dispatcher orderDispatcher = null;
    if (dispatcherSettings != null) {
      //try {
        orderDispatcher = new Dispatcher(dispatcherSettings);
        orderDispatcher.start();
      //} catch (LifeCycleException _ex) {
      //  System.err.println("ERROR: cannot instantiate file system dispatcher: " + _ex.toString());
      //  orderDispatcher = null;
      //}
    }

    XMLOrderProcessing orderProcessing = null;
    if(orderDispatcher != null) {
      try {
        orderProcessing = new XMLOrderProcessing(orderDispatcher);
        orderProcessing.start();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: cannot instantiate processing: " + _ex.toString());
        orderProcessing = null;
      }
    }

    LifeCycle orderReceiver = null;
    if (receiverSettings != null && orderProcessing != null) {
      try {
        orderReceiver = new Receiver(receiverSettings, orderProcessing);
        orderReceiver.start();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: cannot start order receiver: " + _ex.toString());
        orderReceiver = null;
      }
    }

    //
    // TODO: rudimentary wait for execution
    //
    try {
      System.in.read();
    } catch (IOException _ex) {
      // nop
    }

    //
    // tear down the pipeline forwards, starting from the input
    //
    if (orderReceiver != null) {
      try {
        orderReceiver.stop();
      } catch(LifeCycleException _ex) {
        System.err.println("ERROR: failed to stop the receiver: " + _ex.toString());
      } finally {
        orderReceiver = null;
      }
    }

    if(orderProcessing != null) {
      try {
        orderProcessing.stop();
      } catch(LifeCycleException _ex) {
        System.err.println("ERROR: failed to stop the processing: " + _ex.toString());
      } finally {
        orderProcessing = null;
      }
    }

    if (orderDispatcher != null) {
      try {
        orderDispatcher.stop();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: failed to stop the dispatcher: " + _ex.toString());
      } finally {
        orderDispatcher = null;
      }
    }

    System.exit(0);
  }
}
