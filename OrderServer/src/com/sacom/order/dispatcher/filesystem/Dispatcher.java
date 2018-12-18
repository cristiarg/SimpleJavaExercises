package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.common.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Dispatcher implements LifeCycle, MessageDispatcher, MessageBrokerClient {
  private DispatcherSettings settings;

  private ExecutorService executor;

  public Dispatcher(DispatcherSettings _settings) {
    settings = _settings;
  }

  @Override
  public synchronized void start() /*throws LifeCycleException*/ {
    if (executor == null) {
      logStatusBeforeStart();

      executor = Executors.newWorkStealingPool();

      logStatusAfterStart();
    }
  }

  @Override
  public synchronized void stop() throws LifeCycleException {
    if (executor != null) {
      logStatusBeforeStop();

      try {
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS); // TODO: configurable
        if(executor.isShutdown()) {
          executor = null;
        } else {
          throw new LifeCycleException("File System Dispatcher: cannot shutdown");
        }
      } catch(InterruptedException _ex) {
        throw new LifeCycleException("File System Dispatcher: termination failed", _ex);
      }

      logStatusAfterStop();
    }
  }

  private void logStatusBeforeStart() {
    System.out.println("INFO: File System Dispatcher:");
    System.out.println("      starting..");
  }

  private void logStatusAfterStart() {
    System.out.println("INFO: File System Dispatcher:");
    System.out.println("      saving processed order XML files into '" + settings.getDirectory() + "'");
  }

  private void logStatusBeforeStop() {
    System.out.println("INFO: File System Dispatcher:");
    System.out.println("      stopping..");
  }

  private void logStatusAfterStop() {
    System.out.println("INFO: File System Dispatcher:");
    System.out.println("      stopped");
  }


  @Override
  public synchronized void dispatch(final String _interest, final MessageDescription _messageDescription) {
    if (executor != null) {
      final String nature = _messageDescription.getNature();
      if (nature.equals("processing")) {
        // TODO: not interested in the result, for now
        // TODO: get the future instance, add it to a waiting queue, dispatch another thread to
        // monitor the queue
        executor.submit(new OrderHandler(settings, _messageDescription));
      } else {
        // TODO: not the order description we've been waiting for
        System.err.println("ERROR: Dispatcher: unexpected nature; found '" + nature + "'; expected 'processing'; message will be discarded");
      }
    }
  }

  @Override
  public void register(MessageBrokerServer _messageBrokerServer) {
    // registering ourselves
    _messageBrokerServer.subscribe("processing", this);
  }
}
