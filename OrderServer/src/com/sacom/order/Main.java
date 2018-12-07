package com.sacom.order;

import com.sacom.order.receiver.ReceiverException;
import com.sacom.order.receiver.filesystem.FileSystemReceiver;
import com.sacom.order.receiver.OrderReceiver;
import com.sacom.order.receiver.filesystem.FileSystemReceiverSettings;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main( String[] args ) {

    OrderReceiver orderReceiver = null;
    try {
      FileSystemReceiverSettings receiverFileSystemSettings
          = new FileSystemReceiverSettings(
              "C:\\_", 100, TimeUnit.MILLISECONDS );

      orderReceiver = new FileSystemReceiver(receiverFileSystemSettings);
      orderReceiver.start();

    } catch ( ReceiverException _ex) {
      System.err.println( "ERROR: cannot instantiate file system receiver settings: " + _ex.toString() );
      System.exit( -1 );
    }

    try {
      System.in.read();
      if (orderReceiver != null) {
        orderReceiver.stop();
      }
    } catch ( IOException _ex ) {
      // nop
      System.exit( -1 );
    }

    System.exit( 0 );
  }
}
