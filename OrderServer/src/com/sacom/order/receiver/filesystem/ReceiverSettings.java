package com.sacom.order.receiver.filesystem;

import com.sacom.order.receiver.ReceiverException;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ReceiverSettings {
  private String directory;
  private int directoryTimeOut;
  private TimeUnit directoryTimeoutUnit;

  public ReceiverSettings(String _directory, int _directoryTimeOut,
                          TimeUnit _directoryTimeoutUnit) throws ReceiverException {
    directory = _directory;
    directoryTimeOut = _directoryTimeOut;
    directoryTimeoutUnit = _directoryTimeoutUnit;

    check();
  }

  private void check() throws ReceiverException {
    if (getDirectory().length() == 0) {
      throw new ReceiverException("Configuration: Directory value is empty.");
    }
    File d = new File(getDirectory());
    if (!d.exists()) {
      throw new ReceiverException("Configuration: Directory does not exist");
    }
    if (!d.isDirectory()) {
      throw new ReceiverException("Configuration: Not a directory.");
    }
    directory = d.getAbsolutePath().toString();

    // there is no 'unsigned int'
    if (getDirectoryTimeOut() < 0) {
      throw new ReceiverException("Configuration: Negative timeout.");
    }
  }

  synchronized String getDirectory() {
    return directory;
  }

  synchronized int getDirectoryTimeOut() {
    return directoryTimeOut;
  }

  synchronized TimeUnit getDirectoryTimeoutUnit() {
    return directoryTimeoutUnit;
  }
}
