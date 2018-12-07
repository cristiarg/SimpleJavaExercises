package com.sacom.order.model;

import java.nio.file.Path;

public class IncomingOrderDescription {
  private Path directoryPath;
  private Path fileName;

  public IncomingOrderDescription( Path _directoryPath, Path _fileName ) {
    directoryPath = _directoryPath;
    fileName = _fileName;
  }

  public Path getDirectoryPath() {
    return directoryPath;
  }

  public Path getFileName() {
    return fileName;
  }
}
