package com.matheus.springwebfluxdynamodbpoc.service;

import com.matheus.springwebfluxdynamodbpoc.model.Book;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

@Service
public class BookService {

  private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

  public BookService(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
    this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
  }

  public Flux<Book> findAll() {
    return Flux.just(dynamoDbEnhancedAsyncClient.table(
                Book.BOOK_TABLE, TableSchema.fromBean(Book.class)))
        .map(DynamoDbAsyncTable::scan)
        .flatMap(PagePublisher::items);
  }

  public Mono<Book> findByIsbn(final String isbn) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Book.BOOK_TABLE,
            TableSchema.fromBean(Book.class)))
        .map(table -> table.getItem(Key.builder().partitionValue(isbn).build()))
        .map(CompletableFuture::join);
  }

  public Mono<Book> add(final Book book) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Book.BOOK_TABLE,
            TableSchema.fromBean(Book.class)))
        .map(table -> table.putItem(book))
        .thenReturn(book);
  }
}
