package com.sacom.order.common;

/**
 * minimal start/stop interface
 */
public interface LifeCycle {
  /**
   * non-blocking
   *
   * @throws LifeCycleException
   */
  void start() throws LifeCycleException;

  /**
   * blocking
   *
   * @throws LifeCycleException
   */
  void stop() throws LifeCycleException;
}
