package com.sacom.order.processing;

import java.util.concurrent.TimeUnit;

public class ProcessingSettings {
  private final String xsdSchemaFileName;
  private final boolean cleanUpProcessedOrderFiles;

  private final int tryCount = 20;
  private final int tryDelayMilli = 100;

  public ProcessingSettings(final String _xsdSchemaFileName, final boolean _cleanUpProcessedOrderFiles) {
    xsdSchemaFileName = _xsdSchemaFileName;
    cleanUpProcessedOrderFiles = _cleanUpProcessedOrderFiles;
  }

  String getXsdSchemaFileName() {
    return xsdSchemaFileName;
  }

  boolean isCleanUpProcessedOrderFiles() {
    return cleanUpProcessedOrderFiles;
  }

  int getTryCount() {
    return tryCount;
  }

  int getTryDelayMilli() {
    return tryDelayMilli;
  }
}
