package com.sacom.order.dispatcher;

public class DispatcherException extends Exception {
  public DispatcherException(String _message) {
    super(_message);
  }

  public DispatcherException(String _message, Throwable _cause) {
    super(_message, _cause);
  }
}
