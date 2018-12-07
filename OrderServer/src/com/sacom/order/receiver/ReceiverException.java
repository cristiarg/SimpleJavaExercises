package com.sacom.order.receiver;

public class ReceiverException extends Exception {
  public ReceiverException(String _message) {
    super(_message);
  }

  public ReceiverException(String _message, Throwable _cause) {
    super(_message, _cause);
  }
}
