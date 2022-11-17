package com.matheus.springwebfluxdynamodbpoc.service;

import com.matheus.springwebfluxdynamodbpoc.model.Book;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Service
public class BookService {

  private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

  public BookService(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
    this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
  }

  public Flux<Book> findAll() {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(
            Book.TABLE_NAME, TableSchema.fromBean(Book.class)))
        .map(DynamoDbAsyncTable::scan)
        .flatMapMany(PagePublisher::items);
  }

  public Mono<Book> findByIsbn(final String isbn) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Book.TABLE_NAME,
            TableSchema.fromBean(Book.class)))
        .map(table -> table.getItem(Key.builder().partitionValue(isbn).build()))
        .map(CompletableFuture::join);
  }

  public Flux<Book> findByIsbnAndName(final String isbn, final String name) {
    return Mono.just(
            dynamoDbEnhancedAsyncClient.table(Book.TABLE_NAME, TableSchema.fromBean(Book.class)))
        .map(table -> table.query(QueryEnhancedRequest.builder()
            .filterExpression(
                Expression.builder()
                    .expression("name = :nameValue")
                    .expressionValues(Map.of(":nameValue", AttributeValue.builder().s(name).build()))
                    .build())
            .queryConditional(QueryConditional.keyEqualTo(
                Key.builder().partitionValue(isbn).build())).build()))
        .flatMapMany(PagePublisher::items);
  }

  public Mono<Book> add(final Book book) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Book.TABLE_NAME,
            TableSchema.fromBean(Book.class)))
        .map(table -> table.putItem(book))
        .thenReturn(book);
  }
}
