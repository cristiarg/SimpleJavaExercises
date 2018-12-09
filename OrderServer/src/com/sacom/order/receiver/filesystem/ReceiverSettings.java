package com.sacom.order.receiver.filesystem;

import com.sacom.order.receiver.ReceiverException;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ReceiverSettings {
  private String directory;
  private int directoryTimeOut;
  private TimeUnit directoryTimeoutUnit;
  private Pattern regexPattern;

  public ReceiverSettings(String _directory, int _directoryTimeOut,
                          TimeUnit _directoryTimeoutUnit) throws ReceiverException {
    directory = _directory;
    directoryTimeOut = _directoryTimeOut;
    directoryTimeoutUnit = _directoryTimeoutUnit;

    check();
    // prepare a, for the moment, hard-coded regex for the file name
    prepareRegex();
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

  private void prepareRegex() {
    regexPattern = Pattern.compile("orders(\\d\\d)\\.xml", Pattern.CASE_INSENSITIVE);
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

  synchronized Pattern getRegexPattern() {
    return regexPattern;
  }
}
