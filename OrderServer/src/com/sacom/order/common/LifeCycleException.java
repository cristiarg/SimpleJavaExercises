package com.sacom.order.common;

public class LifeCycleException extends Exception {
  public LifeCycleException(String _message) {
    super(_message);
  }

  public LifeCycleException(String _message, Throwable _cause) {
    super(_message, _cause);
  }
}
