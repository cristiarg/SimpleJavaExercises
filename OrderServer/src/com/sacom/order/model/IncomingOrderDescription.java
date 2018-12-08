package com.sacom.order.model;

import com.sacom.order.common.OrderDescription;

import java.nio.file.Path;

public class IncomingOrderDescription implements OrderDescription {
  private Path directoryPath;
  private Path fileName;

  public IncomingOrderDescription(Path _directoryPath, Path _fileName) {
    directoryPath = _directoryPath.toAbsolutePath();
    fileName = _fileName;
  }

  @Override
  public Path getDirectoryPath() {
    return directoryPath;
  }

  @Override
  public Path getFileName() {
    return fileName;
  }
}
