package com.sacom.order.processing;

import com.sacom.order.common.OrderDescription;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

class OrderFileUtil {
  private final FileSystem fs = FileSystems.getDefault();

  private Path directoryAsPath;

  private Path initialFileAsPath;
  private File initialFile;

  private File renamedFile;

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

  private File getInitialFile() {
    if (initialFile == null) {
      final Path initialFileFullPathAsPath = fs.getPath(directoryAsPath.toString(),initialFileAsPath.toString());
      initialFile = new File(initialFileFullPathAsPath.toUri());
    }
    return initialFile;
  }

  File getRenamedFile() {
    if (renamedFile == null) {
      final String uuid = UUID.randomUUID().toString();
      final Path renamedFileAsPath = fs.getPath(uuid + "_" + initialFileAsPath.toString());
      final Path renamedFileFullPathAsPath = fs.getPath(directoryAsPath.toString(), renamedFileAsPath.toString());

      renamedFile = new File(renamedFileFullPathAsPath.toUri());
    }
    return renamedFile;
  }

  boolean renameToTemporary() {
    int tryCount = 20; // TODO: from settings
    int tryDelayMilli = 100;
    while(tryCount > 0) {
      final boolean res = getInitialFile().renameTo(getRenamedFile());
      if (res) {
        state = State.Renamed;
        return true;
      } else {
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

  void renameBack() {
    assert state == State.Renamed;
    final boolean res = getRenamedFile().renameTo(getInitialFile());
    if (res) {
      state = State.Initial;
    }
  }

  boolean delete() {
    boolean res = true;
    if (initialFile != null && initialFile.exists()) {
      res = initialFile.delete();
    }
    if (renamedFile != null && renamedFile.exists()) {
      final boolean resRenamed = renamedFile.delete();
      res = res && resRenamed;
    }
    return res;
  }
}
