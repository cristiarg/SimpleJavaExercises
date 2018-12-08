package com.sacom.order;

import com.sacom.order.common.LifeCycleException;
import com.sacom.order.dispatcher.DispatcherException;
import com.sacom.order.dispatcher.filesystem.FileSystemDispatcher;
import com.sacom.order.dispatcher.filesystem.FileSystemDispatcherSettings;
import com.sacom.order.receiver.ReceiverException;
import com.sacom.order.receiver.filesystem.FileSystemReceiver;
import com.sacom.order.common.LifeCycle;
import com.sacom.order.receiver.filesystem.FileSystemReceiverSettings;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    //
    // construct settings
    //
    FileSystemDispatcherSettings dispatcherSettings = null;
    try {
      dispatcherSettings = new FileSystemDispatcherSettings("C:\\_o", 10);
    } catch (DispatcherException _ex) {
      System.err.println("ERROR: cannot instantiate file system dispatcher settings: " + _ex.toString());
      dispatcherSettings = null;
    }

    FileSystemReceiverSettings receiverSettings = null;
    try {
      receiverSettings = new FileSystemReceiverSettings(
          "C:\\_i", 100, TimeUnit.MILLISECONDS);
    } catch (ReceiverException _ex) {
      System.err.println("ERROR: cannot instantiate file system receiver: " + _ex.toString());
      receiverSettings = null;
    }

    //
    // construct the pipeline backwards
    //
    FileSystemDispatcher orderDispatcher = null;
    if (dispatcherSettings != null) {
      try {
        orderDispatcher = new FileSystemDispatcher(dispatcherSettings);
        orderDispatcher.start();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: cannot instantiate file system dispatcher: " + _ex.toString());
        orderDispatcher = null;
      }
    }

    LifeCycle orderReceiver = null;
    if (receiverSettings != null) {
      try {
        orderReceiver = new FileSystemReceiver(receiverSettings, orderDispatcher);
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
