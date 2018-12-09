package com.sacom.order.receiver.filesystem;

import com.sacom.order.common.LifeCycleException;
import com.sacom.order.common.LifeCycle;
import com.sacom.order.common.OrderDispatcher;
import com.sacom.order.receiver.ReceiverException;

/**
 * Monitors a directory on the filesystem, according to settings, for new 'incoming' order files.
 */
public class Receiver implements LifeCycle {
  private ReceiverSettings settings;
  private OrderDispatcher dispatcher;

  private NewFilesWatcher newFilesWatcher;
  private Thread watcherThread;

  public Receiver(ReceiverSettings _settings, OrderDispatcher _dispatcher) {
    settings = _settings;
    dispatcher = _dispatcher;
  }

  @Override
  public void start() throws LifeCycleException {
    if (newFilesWatcher == null && watcherThread == null) {
      try {
        newFilesWatcher = new NewFilesWatcher(settings, dispatcher);
        watcherThread = new Thread(newFilesWatcher);
        watcherThread.start();

      } catch (ReceiverException _ex) {
        throw new LifeCycleException("File System Receiver: cannot start newFilesWatcher thread", _ex);
      }
    } else {
      // already running
      assert watcherThread.getState() != Thread.State.TERMINATED;
    }
  }

  @Override
  public void stop() /*throws LifeCycleException*/ {
    if (newFilesWatcher != null && watcherThread != null) {
      boolean overflowPresent;
      watcherThread.interrupt();
      try {
        watcherThread.join();
      } catch (InterruptedException _ex) {
        // nop
      } finally {
        watcherThread = null;
        overflowPresent = newFilesWatcher.isOverflowDuringMonitoring();
        newFilesWatcher = null;
      }

      if(overflowPresent) {
        // TODO: better error/warning/info message reporting
        System.out.println("WARNING: overflow during file system monitoring");
      }
    }
  }

}
