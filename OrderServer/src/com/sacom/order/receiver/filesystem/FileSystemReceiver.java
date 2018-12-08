package com.sacom.order.receiver.filesystem;

import com.sacom.order.common.LifeCycleException;
import com.sacom.order.common.LifeCycle;
import com.sacom.order.common.OrderDescription;
import com.sacom.order.common.OrderDispatcher;
import com.sacom.order.receiver.ReceiverException;

/**
 * Monitors a directory on the filesystem, according to settings, for new 'incoming' order files.
 */
public class FileSystemReceiver implements LifeCycle {
  private FileSystemReceiverSettings settings;
  private OrderDispatcher dispatcher;

  private FileSystemWatcher watcher; // TODO: member not needed
  private Thread watcherThread;

  public FileSystemReceiver(FileSystemReceiverSettings _settings, OrderDispatcher _dispatcher) {
    settings = _settings;
    dispatcher = _dispatcher;
  }

  @Override
  public void start() throws LifeCycleException {
    if (watcher == null && watcherThread == null) {
      try {
        watcher = new FileSystemWatcher(settings, dispatcher);
        watcherThread = new Thread(watcher);
        watcherThread.start();

      } catch (ReceiverException _ex) {
        throw new LifeCycleException("File System Receiver: cannot start watcher thread", _ex);
      }
    } else {
      // already running
      assert watcherThread.getState() != Thread.State.TERMINATED;
    }
  }

  @Override
  public void stop() {
    if (watcher != null && watcherThread != null) {
      watcherThread.interrupt();
      try {
        watcherThread.join();
      } catch (InterruptedException _ex) {
        // nop
      } finally {
        watcherThread = null;
        watcher = null;
      }
    }
  }

}
