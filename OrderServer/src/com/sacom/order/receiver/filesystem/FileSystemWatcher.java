package com.sacom.order.receiver.filesystem;

import com.sacom.order.model.IncomingOrderDescription;
import com.sacom.order.receiver.ReceiverException;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

class FileSystemWatcher implements Runnable {
  private FileSystemReceiverSettings settings;
  private WatchService watchService;
  private Path directory;

  public FileSystemWatcher(FileSystemReceiverSettings _settings) throws ReceiverException {
    settings = _settings;

    constructAndStartUpWatcher();
      // called here just because we cannot throw from the run method
  }

  @Override
  public void run() {
    while( true ) {
      try {
        WatchKey key = watchService.poll(settings.getDirectoryTimeOut(), settings.getDirectoryTimeoutUnit());
        if (key != null) {
          System.out.println( "====" );
          for (WatchEvent<?> event : key.pollEvents()) {
            //final WatchEvent.Kind<?> typeOfEvent = event.kind();
            //System.out.println( "Kind: " + typeOfEvent.name() + "  " + typeOfEvent.toString() );
            //System.out.println( "Kind: " + typeOfEvent.name() + "  " + typeOfEvent.toString() );
            //final String eventContextName = event.context().toString();
            Path fileNamePath = (Path) event.context();
            //String fileNameAsString = fileName.toString();
            IncomingOrderDescription orderDescription = new IncomingOrderDescription( directory ,
                fileNamePath );
            System.out.println( "Dir: " + directory.toAbsolutePath() );
            System.out.println( "File: " + fileNamePath.toAbsolutePath() );
          }
          key.reset();
        } else {
          final TimeUnit tu = settings.getDirectoryTimeoutUnit();
          final long millis = tu.toMillis( settings.getDirectoryTimeOut() );
          Thread.sleep(millis);
        }
        if (Thread.interrupted()) {
          break;
        }

      } catch( InterruptedException _ex ) {
        break;
      }
    }

    shutDownAndDisposeWatcher();
  }

  private void constructAndStartUpWatcher() throws ReceiverException {
    try {
      watchService = FileSystems.getDefault().newWatchService();
      directory = Paths.get(settings.getDirectory());
      WatchKey registrationKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    } catch( IOException _ex ) {
      throw new ReceiverException( "ERROR: starting watcher: " + _ex.getMessage() , _ex);
    }
  }

  private void shutDownAndDisposeWatcher() {
    if(watchService != null) {
      try {
        watchService.close();
      } catch( IOException _ex ) {
        // TODO: bury..
      } finally {
        watchService = null;
      }
    }

    directory = null;
  }
}
