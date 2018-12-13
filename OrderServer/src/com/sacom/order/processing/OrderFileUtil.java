package com.sacom.order.processing;

import com.sacom.order.common.OrderDescription;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

class OrderFileUtil {
  private final FileSystem fs = FileSystems.getDefault();

  private Path directoryAsPath;

  private Path initialFileAsPath;

  private Path initialPath;

  private Path tempPath;
  private File tempFile;

  enum State {
    Initial,
    Renamed
  }
  private State state;

  OrderFileUtil(final OrderDescription _orderDescription) {
    directoryAsPath = (Path) _orderDescription.item("directory");
    directoryAsPath = directoryAsPath.toAbsolutePath();

    initialFileAsPath = (Path) _orderDescription.item("file");

    state = State.Initial;
  }

  private Path getInitialPath() {
    if (initialPath == null) {
      initialPath = fs.getPath(directoryAsPath.toString(),initialFileAsPath.toString());
    }
    return initialPath;
  }

  void deleteInitialFile() {
    if (initialPath != null) {
      try {
        Files.deleteIfExists(initialPath);
      } catch(IOException _ex) {
        _ex.printStackTrace();
      } finally {
        initialPath = null;
      }
    }
  }

  private Path getTempPath() {
    if (tempPath == null) {
      final String tempDir = System.getProperty("java.io.tmpdir");
      final String uuidFileName = UUID.randomUUID().toString() + ".xml";
      tempPath = Paths.get(tempDir, uuidFileName).toAbsolutePath();
    }
    return tempPath;
  }

  File getTempFile() {
    if(tempFile == null) {
      tempFile = new File(getTempPath().toUri());
    }
    return tempFile;
  }

  void deleteTempFile() {
    if (tempPath != null ) {
      try {
        Files.deleteIfExists(tempPath);
      } catch (IOException _ex) {
        _ex.printStackTrace();
      } finally {
        tempFile = null;
        tempPath = null;
      }
    }
  }

  boolean copyToTemporary() {
    int tryCount = 20; // TODO: from settings
    int tryDelayMilli = 100;
    while(tryCount > 0) {
      final Path initialPath = getInitialPath();
      final Path tempPath = getTempPath();
      try {
        Files.copy(initialPath, tempPath);
        state = State.Renamed;
        return true;
      } catch (IOException _ex) {
        --tryCount;
        try {
          Thread.sleep(tryDelayMilli);
        } catch (InterruptedException e) {
          break;
        }
      }
    }
    return false;
  }
}
