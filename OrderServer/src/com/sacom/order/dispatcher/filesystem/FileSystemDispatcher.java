package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.common.LifeCycle;
import com.sacom.order.common.LifeCycleException;
import com.sacom.order.common.OrderDescription;
import com.sacom.order.common.OrderDispatcher;
import com.sacom.order.dispatcher.DispatcherException;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileSystemDispatcher implements LifeCycle, OrderDispatcher {
  private ExecutorService executor;

  FileSystemDispatcherSettings settings;

  public FileSystemDispatcher(FileSystemDispatcherSettings _settings) {
    settings = _settings;
  }

  @Override
  public void start() throws LifeCycleException {
    if (executor == null) {
       executor = Executors.newWorkStealingPool();
    }
  }

  @Override
  public void stop() throws LifeCycleException {
    if (executor != null) {
      try {
        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        executor.shutdown();
        if(executor.isShutdown()) {
          executor = null;
        } else {
          throw new LifeCycleException("File System Dispatcher: cannot shutdown");
        }
      } catch(InterruptedException _ex) {
        throw new LifeCycleException("File System Dispatcher: termination failed", _ex);
      }
    }
  }

  @Override
  public void dispatch(OrderDescription _orderDescription) {
    if (executor != null) {
      // TODO: not interested in the result, for now
      // TODO: get the future instance, add it to a waiting queue, dispatch another thread to
      // monitor the queue
      executor.submit(new OrderHandler(settings, _orderDescription));
    }
  }
}
