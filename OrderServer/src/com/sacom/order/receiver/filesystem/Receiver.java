package com.sacom.order.receiver.filesystem;

import com.sacom.order.common.*;
import com.sacom.order.receiver.ReceiverException;

/**
 * Monitors a directory on the filesystem, according to settings, for new 'incoming' order files.
 */
public class Receiver implements LifeCycle, MessageBrokerClient {
  private ReceiverSettings settings;
  private MessageDispatcher messageDispatcher;

  private NewFilesWatcher newFilesWatcher;
  private Thread watcherThread;

  public Receiver(ReceiverSettings _settings) {
    settings = _settings;
  }

  @Override
  public void start() throws LifeCycleException {
    if (newFilesWatcher == null && watcherThread == null) {
      logStatusBeforeStart();

      try {
        newFilesWatcher = new NewFilesWatcher(settings, messageDispatcher);
        watcherThread = new Thread(newFilesWatcher);
        watcherThread.start();

      } catch (ReceiverException _ex) {
        throw new LifeCycleException("File System Receiver: cannot start newFilesWatcher thread", _ex);
      }

      logStatusAfterStart();

    } else {
      // already running
      assert watcherThread.getState() != Thread.State.TERMINATED;
    }
  }

  @Override
  public void stop() /*throws LifeCycleException*/ {
    if (newFilesWatcher != null && watcherThread != null) {
      logStatusBeforeStop();

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
        System.out.println("WARNING: overflow status received during file system monitoring; this usually means that not all orders have been processed");
      }

      logStatusAfterStop();
    }
  }

  private void logStatusBeforeStart() {
    System.out.println("INFO: File System Receiver:");
    System.out.println("      starting..");
  }

  private void logStatusAfterStart() {
    System.out.println("INFO: File System Receiver:");
    System.out.println("      started monitoring local file system location '" + settings.getDirectory() +
        "' for files matching the pattern '" + settings.getRegexPattern().pattern() + "'");
  }

  private void logStatusBeforeStop() {
    System.out.println("INFO: File System Receiver:");
    System.out.println("      stopping..");
  }

  private void logStatusAfterStop() {
    System.out.println("INFO: File System Receiver:");
    System.out.println("      stopped");
  }

  @Override
  public void register(MessageBrokerServer _messageBrokerServer) {
    // keep a reference to the broker to be able to dispatch messages to it (one way or another)
    messageDispatcher = _messageBrokerServer;
    // not registering ourselves for any messages
  }

  @Override
  public void dispatch(String _interest, MessageDescription _messageDescription) {
    assert "we subscribed to nothinf".length() == 0;
  }
}
