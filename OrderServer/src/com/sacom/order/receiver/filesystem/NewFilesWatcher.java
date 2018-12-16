package com.sacom.order.receiver.filesystem;

import com.sacom.order.common.MessageBrokerServer;
import com.sacom.order.common.MessageDescription;
import com.sacom.order.common.MessageDispatcher;
import com.sacom.order.receiver.ReceiverException;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * implements a watching loop that continuously polls
 */
class NewFilesWatcher implements Runnable {
  private ReceiverSettings settings;
  private MessageDispatcher messageDispatcher;

  private WatchService watchService;
  private Path directory;

  private boolean overflowDuringMonitoring = false;

  NewFilesWatcher(ReceiverSettings _settings, MessageDispatcher _messageDispatcher) throws ReceiverException {
    settings = _settings;
    messageDispatcher = _messageDispatcher;

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
                String orderNumber = extractOrderNumber(fileNamePath.toString());
                if(orderNumber.length() > 0) {
                  try {
                    MessageDescription messageDescription = new MessageDescription("receiver",
                        "directory", directory,
                        "file", fileNamePath,
                        "orderNumber", orderNumber);
                    //System.out.println("Dir: " + directory.toAbsolutePath());
                    //System.out.println("File: " + fileNamePath.toAbsolutePath());
                    messageDispatcher.dispatch("receiver", messageDescription);

                  } catch (Exception _ex) {
                    // TODO:
                    _ex.printStackTrace();
                  }
                }
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

  private String extractOrderNumber(String _fileName) {
    Pattern regexPattern = settings.getRegexPattern();
    Matcher regexMatcher = regexPattern.matcher(_fileName);
    if(regexMatcher.matches()) {
      String s = regexMatcher.group(1);
      if (s.length() > 0) {
        return s;
      }
    }
    return "";
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
