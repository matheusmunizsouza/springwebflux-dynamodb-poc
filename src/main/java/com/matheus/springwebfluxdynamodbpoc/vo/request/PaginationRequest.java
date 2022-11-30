package com.matheus.springwebfluxdynamodbpoc.vo.request;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public final class PaginationRequest {

  private final int limit;
  private final Map<String, AttributeValue> lastEvaluatedKey;

  PaginationRequest(Integer limit, String lastEvaluatedKey) {
    this.limit = limit == null ? 10 : limit;

    if (StringUtils.hasLength(lastEvaluatedKey)) {
      HashMap<String, AttributeValue> keys = new HashMap<>();
      for (String keyValue : lastEvaluatedKey.split(";")) {
        String[] split = keyValue.split(":");
        keys.put(split[0], AttributeValue.builder().s(split[1]).build());
      }
      this.lastEvaluatedKey = keys;
    } else {
      this.lastEvaluatedKey = null;
    }
  }

  public int getLimit() {
    return limit;
  }

  public Map<String, AttributeValue> getLastEvaluatedKey() {
    return lastEvaluatedKey;
  }
}
