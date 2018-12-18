package com.sacom.order.common;

/**
 * an implementer has the liberty of subscribing to more than one type of
 * messages from the message broker
 */
public interface MessageBrokerClient extends MessageDispatcher {
  void register(MessageBrokerServer _messageBrokerServer);
}
