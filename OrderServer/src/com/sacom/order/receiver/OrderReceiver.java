package com.sacom.order.receiver;

/**
 * Interface to start stop an order receiver
 */
public interface OrderReceiver {
  /**
   * non-blocking
   * @throws ReceiverException
   */
  void start() throws ReceiverException;

  void stop();
}
