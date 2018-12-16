package com.sacom.order.backbone;

import com.sacom.order.common.MessageBrokerServer;
import com.sacom.order.common.MessageDescription;
import com.sacom.order.common.MessageDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageBroker implements MessageBrokerServer {
  /**
   * keep a per-interest list of dispatchers
   * one dispatcher instance can subscribe to more than one interest
   */
  private Map<String, List<MessageDispatcher>> interestMap = new HashMap<>();

  @Override
  public synchronized boolean subscribe(final String _interest, final MessageDispatcher _messageDispatcher) {
    List<MessageDispatcher> orderList = getOrCreateList(_interest);
    if(!isOrderDispatcherSubscribed(_messageDispatcher, orderList)) {
      orderList.add(_messageDispatcher);
      return true;
    }
    return false;
  }

  @Override
  public void dispatch(final String _interest, final MessageDescription _messageDescription) {
    final List<MessageDispatcher> list = getList(_interest);
    if (list != null) {
      for(final MessageDispatcher messageDispatcher : list) {
        messageDispatcher.dispatch(_interest, _messageDescription);
      }
    }
  }

  private List<MessageDispatcher> getList(final String _interest) {
    if(interestMap.containsKey(_interest)) {
      return interestMap.get(_interest);
    }
    return null;
  }

  private List<MessageDispatcher> getOrCreateList(final String _interest) {
    List<MessageDispatcher> orderList = getList(_interest);
    if (orderList == null) {
      orderList = new ArrayList<MessageDispatcher>();
      interestMap.put(_interest, orderList);
    }
    return orderList;
  }

  private boolean isOrderDispatcherSubscribed(final MessageDispatcher _messageDispatcher, final List<MessageDispatcher> _messageDispatcherList) {
    for(MessageDispatcher od : _messageDispatcherList) {
      if(od == _messageDispatcher) {
        return true;
      }
    }
    return false;
  }
}
