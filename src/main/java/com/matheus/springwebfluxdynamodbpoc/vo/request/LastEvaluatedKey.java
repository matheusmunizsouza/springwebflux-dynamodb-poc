package com.matheus.springwebfluxdynamodbpoc.vo.request;

import java.util.HashMap;
import java.util.Map;

public final class LastEvaluatedKey {

  private final Map<String, String> lastKeys;

  public LastEvaluatedKey(String lastKeys) {
    HashMap<String, String> keys = new HashMap<>();
    for (String keyValue : lastKeys.split(";")) {
      String[] split = keyValue.split(":");
      keys.put(split[0], split[1]);
    }
    this.lastKeys = keys;
  }

  public Map<String, String> getLastKeys() {
    return lastKeys;
  }
}
