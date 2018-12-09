package com.sacom.order.receiver.filesystem;

import com.sacom.order.common.OrderDispatcher;
import com.sacom.order.model.IncomingOrderDescription;
import com.sacom.order.receiver.ReceiverException;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * implements a watching loop that continuously polls
 */
class NewFilesWatcher implements Runnable {
  private ReceiverSettings settings;
  private OrderDispatcher dispatcher;

  private WatchService watchService;
  private Path directory;

  private boolean overflowDuringMonitoring = false;

  NewFilesWatcher(ReceiverSettings _settings, OrderDispatcher _dispatcher) throws ReceiverException {
    settings = _settings;
    dispatcher = _dispatcher;

    constructAndStartUpWatcher();
    // called here just because we cannot throw from the run method
  }

  @Override
  public void run() {
    try {
      while (true) {
        try {
          final WatchKey key = watchService.poll(settings.getDirectoryTimeOut(), settings.getDirectoryTimeoutUnit());

          if (key != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
              final WatchEvent.Kind<?> eventKind = event.kind();
              if (eventKind == StandardWatchEventKinds.OVERFLOW) {
                overflowDuringMonitoring = true;
              } else {
                final Path fileNamePath = (Path) event.context();
                IncomingOrderDescription orderDescription = new IncomingOrderDescription(directory, fileNamePath);
                //System.out.println("Dir: " + directory.toAbsolutePath());
                //System.out.println("File: " + fileNamePath.toAbsolutePath());
                dispatcher.dispatch(orderDescription);
              }
            }
            key.reset();
          } else {
            final TimeUnit tu = settings.getDirectoryTimeoutUnit();
            final long millis = tu.toMillis(settings.getDirectoryTimeOut());
            Thread.sleep(millis);
          }

          if (Thread.interrupted()) {
            break;
          }

        } catch (InterruptedException _ex) {
          break;
        }
      }
    } finally {
      shutDownAndDisposeWatcher();
    }
  }

  private void constructAndStartUpWatcher() throws ReceiverException {
    try {
      watchService = FileSystems.getDefault().newWatchService();
      directory = Paths.get(settings.getDirectory());
      /*WatchKey registrationKey =*/
      directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

    } catch (IOException _ex) {
      throw new ReceiverException("ERROR: starting watcher: " + _ex.getMessage(), _ex);
    }
  }

  private void shutDownAndDisposeWatcher() {
    if (watchService != null) {
      try {
        watchService.close();
      } catch (IOException _ex) {
        // TODO: bury..
      } finally {
        watchService = null;
      }
    }

    directory = null;
  }

  boolean isOverflowDuringMonitoring() {
    return overflowDuringMonitoring;
  }
}
