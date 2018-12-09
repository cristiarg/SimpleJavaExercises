package com.sacom.order.common;

/**
 * minimal start/stop interface
 */
public interface LifeCycle {
  /**
   * non-blocking
   *
   * @throws LifeCycleException - details why lifecycle cannot be started
   */
  void start() throws LifeCycleException;

  /**
   * blocking
   *
   * @throws LifeCycleException - details why lifecycle cannot be stopped
   */
  void stop() throws LifeCycleException;
}
