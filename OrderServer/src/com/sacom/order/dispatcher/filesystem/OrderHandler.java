package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.common.OrderDescription;

import java.io.IOException;
import java.nio.file.*;

class OrderHandler implements Runnable {
  DispatcherSettings settings;
  OrderDescription orderDescription;

  OrderHandler(DispatcherSettings _settings, OrderDescription _orderDescription) {
    settings = _settings;
    orderDescription = _orderDescription;
  }

  @Override
  public void run() {
    FileSystem fs = FileSystems.getDefault();

    String oldDirectoryAbsoutePath = orderDescription.getDirectoryPath().toString();
    String fileName = orderDescription.getFileName().toString();

    Path oldFileAbsolutePathAndName = fs.getPath(oldDirectoryAbsoutePath, fileName);
    Path newFileAbsolutePathAndName = fs.getPath(settings.getDirectory(), fileName);

    // large files take some time to finish copying; therefore, give it a few more tries
    int tryCount = 1 + settings.getTryCount();
    while (tryCount > 0) {
      try {
        Files.move(oldFileAbsolutePathAndName, newFileAbsolutePathAndName);
        tryCount = 0;
      } catch (IOException _exIO) {
        --tryCount;
        if (tryCount == 0) {
          // TODO: more appropriate exception handling
          System.err.println("WARNING: moving file '" + fileName + "' failed; will try again: " + _exIO.toString());
        } else {
          // give it some time before trying again
          try {
            Thread.sleep(50);
          } catch (InterruptedException _exTh) {
            // TODO: better
          }
        }
      }
    }
  }

  //// Callable
  //@Override
  //public Object call() throws Exception {
  //  return null;
  //}
}


