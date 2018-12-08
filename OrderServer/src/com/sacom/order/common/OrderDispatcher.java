package com.sacom.order.common;

/**
 * process orders in a non-blocking way
 */
public interface OrderDispatcher {
  /**
   * non-blocking, if possible
   *
   * @param _orderDescription
   */
  void dispatch(OrderDescription _orderDescription);
}
