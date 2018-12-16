package com.sacom.order.common;

public interface MessageBrokerServer extends MessageDispatcher {
  boolean subscribe(final String _interest, final MessageDispatcher _messageDispatcher);
}
