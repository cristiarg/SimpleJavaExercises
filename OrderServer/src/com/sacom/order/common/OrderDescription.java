package com.sacom.order.common;

import java.util.HashMap;
import java.util.Map;

/**
 * General container for messages.
 */
public class OrderDescription {
  private String nature;
  private Map<String, Object> nameValueMap = new HashMap<>();

  /**
   * @param _nature         - a string that identifies the nature of the message; it should be considered
   *                        as part of the application protocol between a produce and a consumer
   * @param _nameValuePairs - names (as String) and values supplied as elements in a map
   * @throws Exception - a message describing what is not right is the constructor's arguments
   */
  public OrderDescription(final String _nature, final Object... _nameValuePairs) throws Exception {
    nature = _nature;

    final int count = _nameValuePairs.length;
    if (count % 2 != 0) {
      throw new Exception("An even number of variable arguments is expected. Found: " + count);
    }

    int i = 0;
    String name = "";
    for (Object o : _nameValuePairs) {
      final boolean isEven = (i % 2 == 0);
      if (isEven) {
        if (!(o instanceof String)) {
          throw new Exception("Name (as String)/Value (as Object) pairs are expected. Found: " + o.getClass().toString());
        }
        name = (String) o;
        if (nameValueMap.containsKey(name)) {
          throw new Exception("Unique names are expected. Found non-unique name: '" + name + "'");
        }
        if (name.length() == 0) {
          throw new Exception("Name are expected to be non-empty.");
        }
      } else {
        nameValueMap.put(name, o);
        name = "";
      }
      ++i;
    }
  }

  /**
   * @return the 'nature' of this message; part of the application level protocol
   * between a producer and a consumer of messages
   */
  public String getNature() {
    return nature;
  }

  public Object item(final String _name) {
    if (nameValueMap.containsKey(_name)) {
      return nameValueMap.get(_name);
    }
    return null;
  }
}
