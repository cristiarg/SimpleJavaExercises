package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.dispatcher.DispatcherException;

import java.io.File;

public class DispatcherSettings {
  private String directory;

  public DispatcherSettings(String _directory) throws DispatcherException {
    directory = _directory;

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
  }

  synchronized String getDirectory() {
    return directory;
  }
}
