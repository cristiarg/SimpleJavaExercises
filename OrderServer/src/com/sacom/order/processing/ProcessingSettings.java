package com.sacom.order.processing;

public class ProcessingSettings {
  private String xsdSchemaFileName;
  private boolean cleanUpProcessedOrderFiles;

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
}
