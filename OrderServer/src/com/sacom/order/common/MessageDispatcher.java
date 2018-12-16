package com.sacom.order.common;

public interface MessageDispatcher {
  void dispatch(final String _interest, final MessageDescription _messageDescription);
}
