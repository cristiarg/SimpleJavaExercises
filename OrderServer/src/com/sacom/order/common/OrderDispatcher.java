package com.sacom.order.common;

/**
 * process orders in a non-blocking way
 */
public interface OrderDispatcher {
  /**
   * to be implemented as-much-as-possible non-blocking
   *
   */
  void dispatch(OrderDescription _orderDescription);
}
