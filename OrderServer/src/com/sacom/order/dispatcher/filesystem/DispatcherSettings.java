package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.dispatcher.DispatcherException;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DispatcherSettings {
  private String directory;
  private int tryCount;

  public DispatcherSettings(String _directory, int _tryCount) throws DispatcherException {
    directory = _directory;
    tryCount = _tryCount;

    check();
  }

  private void check() throws DispatcherException {
    if (directory.length() == 0) {
      throw new DispatcherException("Directory value is empty.");
    }
    File d = new File(directory);
    if (!d.exists()) {
      throw new DispatcherException("Directory does not exist");
    }
    if (!d.isDirectory()) {
      throw new DispatcherException("Not a directory.");
    }
    directory = d.getAbsolutePath().toString();

    if(tryCount < 0) {
      throw new DispatcherException("Number of try cannot be a negative number.");
    }
  }

  synchronized String getDirectory() {
    return directory;
  }

  synchronized int getTryCount() {
    return tryCount;
  }
}
