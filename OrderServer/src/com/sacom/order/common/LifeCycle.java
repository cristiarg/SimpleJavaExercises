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
   */
  void stop() throws LifeCycleException;
}
