package com.sacom.order.common;

import java.nio.file.Path;

public interface OrderDescription {
  Path getDirectoryPath();

  Path getFileName();
}
