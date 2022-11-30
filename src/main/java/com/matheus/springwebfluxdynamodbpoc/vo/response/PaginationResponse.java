package com.matheus.springwebfluxdynamodbpoc.vo.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public final class PaginationResponse<T> {

  private final List<T> items;
  private final int size;
  private final Map<String, String> lastEvaluatedKey;

  private PaginationResponse(final List<T> items, final int size, final Map<String, String> lastEvaluatedKey) {
    this.items = items;
    this.size = size;
    this.lastEvaluatedKey = lastEvaluatedKey;
  }

  public static <T> PaginationResponse<T> from(final Page<T> page) {
    HashMap<String, String> keys = new HashMap<>();
    if (page.lastEvaluatedKey() != null) {
      page.lastEvaluatedKey().forEach((key, attributeValue) -> keys.put(key, attributeValue.s()));
    }
    return new PaginationResponse<>(page.items(), page.items().size(), keys);
  }

  public List<T> getItems() {
    return items;
  }

  public int getSize() {
    return size;
  }

  public Map<String, String> getLastEvaluatedKey() {
    return lastEvaluatedKey;
  }
}
