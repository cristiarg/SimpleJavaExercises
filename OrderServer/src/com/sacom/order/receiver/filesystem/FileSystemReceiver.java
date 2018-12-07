package com.sacom.order.receiver.filesystem;

import com.sacom.order.receiver.OrderReceiver;
import com.sacom.order.receiver.ReceiverException;

/**
 * Monitors a directory on the filesystem, according to settings, for new 'incoming' order files.
 */
public class FileSystemReceiver implements OrderReceiver {
  private FileSystemReceiverSettings settings;
  private FileSystemWatcher watcher;
  private Thread watcherThread;

  public FileSystemReceiver(FileSystemReceiverSettings _settings) {
    settings = _settings;
  }

  @Override
  public void start() throws ReceiverException {
    if(watcher == null && watcherThread == null) {
      try {
        watcher = new FileSystemWatcher( settings );
        watcherThread = new Thread(watcher);
        watcherThread.start();

      } catch (ReceiverException _ex) {
        throw new ReceiverException( "ERROR: canno start watcher thread" , _ex );
      }
    } else {
      // already running
      assert watcherThread.getState() != Thread.State.TERMINATED;
    }
  }

  @Override
  public void stop() {
    if (watcher != null && watcherThread != null){
      watcherThread.interrupt();
      try {
        watcherThread.join();
      } catch(InterruptedException _ex) {
        // nop
      } finally {
        watcherThread = null;
        watcher = null;
      }
    }
  }

}
