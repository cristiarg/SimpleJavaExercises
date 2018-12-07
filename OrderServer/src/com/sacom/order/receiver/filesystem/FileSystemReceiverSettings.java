package com.sacom.order.receiver.filesystem;

import com.sacom.order.receiver.ReceiverException;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class FileSystemReceiverSettings {
  private String directory;
  private int directoryTimeOut;
  private TimeUnit directoryTimeoutUnit;

  public FileSystemReceiverSettings( String _directory , int _directoryTimeOut ,
                                     TimeUnit _directoryTimeoutUnit ) throws ReceiverException {
    directory = _directory;
    directoryTimeOut = _directoryTimeOut;
    directoryTimeoutUnit = _directoryTimeoutUnit;

    check();
  }

  private void check() throws ReceiverException {
    if (getDirectory().length() == 0) {
      throw new ReceiverException( "Directory value is empty." );
    }

    File file = new File(getDirectory());
    if (!file.exists()) {
      throw new ReceiverException( "Directory does not exist" );
    }

    if (!file.isDirectory()) {
      throw new ReceiverException( "Not a directory." );
    }
  }

  public String getDirectory() {
    return directory;
  }

  public int getDirectoryTimeOut() {
    return directoryTimeOut;
  }

  public TimeUnit getDirectoryTimeoutUnit() {
    return directoryTimeoutUnit;
  }

}
