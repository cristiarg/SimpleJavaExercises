package com.sacom.order.dispatcher.filesystem;

import com.sacom.order.common.LifeCycle;
import com.sacom.order.common.LifeCycleException;
import com.sacom.order.common.OrderDescription;
import com.sacom.order.common.OrderDispatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Dispatcher implements LifeCycle, OrderDispatcher {
  private ExecutorService executor; // TODO: extract some utility of this for use in here and in receiver

  DispatcherSettings settings;

  public Dispatcher(DispatcherSettings _settings) {
    settings = _settings;
  }

  @Override
  public synchronized void start() throws LifeCycleException {
    if (executor == null) {
      executor = Executors.newWorkStealingPool();
    }
  }

  @Override
  public synchronized void stop() throws LifeCycleException {
    if (executor != null) {
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
    }
  }

  @Override
  public synchronized void dispatch(OrderDescription _orderDescription) {
    if (executor != null) {
      // TODO: not interested in the result, for now
      // TODO: get the future instance, add it to a waiting queue, dispatch another thread to
      // monitor the queue
      executor.submit(new OrderHandler(settings, _orderDescription));
    }
  }
}
