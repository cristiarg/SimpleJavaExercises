package com.sacom.order.receiver.filesystem;

import com.sacom.order.receiver.ReceiverException;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class FileSystemReceiverSettings {
  private String directory;
  private int directoryTimeOut;
  private TimeUnit directoryTimeoutUnit;

  public FileSystemReceiverSettings(String _directory, int _directoryTimeOut,
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

  public synchronized String getDirectory() {
    return directory;
  }

  public synchronized int getDirectoryTimeOut() {
    return directoryTimeOut;
  }

  public synchronized TimeUnit getDirectoryTimeoutUnit() {
    return directoryTimeoutUnit;
  }
}
