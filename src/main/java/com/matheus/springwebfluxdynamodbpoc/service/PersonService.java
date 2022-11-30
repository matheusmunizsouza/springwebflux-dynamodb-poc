package com.matheus.springwebfluxdynamodbpoc.service;

import com.matheus.springwebfluxdynamodbpoc.model.Person;
import com.matheus.springwebfluxdynamodbpoc.vo.request.PaginationRequest;
import com.matheus.springwebfluxdynamodbpoc.vo.response.PaginationResponse;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Service
public class PersonService {

  private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

  public PersonService(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
    this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
  }

  public Flux<Person> findAll() {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(
            Person.TABLE_NAME, TableSchema.fromBean(Person.class)))
        .map(DynamoDbAsyncTable::scan)
        .flatMapMany(PagePublisher::items);
  }

  public Mono<PaginationResponse<Person>> findByFirstName(final String firstName,
      PaginationRequest paginationRequest) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Person.TABLE_NAME,
            TableSchema.fromBean(Person.class)))
        .flatMap(table -> Mono.from(table.query(QueryEnhancedRequest.builder()
            .queryConditional(
                QueryConditional.keyEqualTo(Key.builder().partitionValue(firstName).build()))
            .limit(paginationRequest.getLimit())
            .exclusiveStartKey(paginationRequest.getLastEvaluatedKey())
            .build())))
        .map(PaginationResponse::from);
  }

  public Mono<Person> findByFirstNameAndLastName(final String firstName, final String lastName) {
    return Mono.just(
            dynamoDbEnhancedAsyncClient.table(Person.TABLE_NAME, TableSchema.fromBean(Person.class)))
        .map(table -> table.getItem(
            Key.builder()
                .partitionValue(firstName)
                .sortValue(lastName)
                .build()))
        .map(CompletableFuture::join);
  }

  public Mono<PaginationResponse<Person>> findByCpf(final String cpf,
      PaginationRequest paginationRequest) {
    return Mono.just(
            dynamoDbEnhancedAsyncClient.table(Person.TABLE_NAME, TableSchema.fromBean(Person.class))
                .index("cpf_index"))
        .flatMap(table -> Mono.from(table.query(QueryEnhancedRequest.builder()
            .queryConditional(
                QueryConditional.keyEqualTo(Key.builder().partitionValue(cpf).build()))
            .limit(paginationRequest.getLimit())
            .exclusiveStartKey(paginationRequest.getLastEvaluatedKey())
            .build())))
        .map(PaginationResponse::from);
  }

  public Mono<Person> add(final Person person) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Person.TABLE_NAME,
            TableSchema.fromBean(Person.class)))
        .map(table -> table.putItem(person))
        .thenReturn(person);
  }

  public Mono<Person> delete(String firstname) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Person.TABLE_NAME,
            TableSchema.fromBean(Person.class)))
        .map(table -> table.deleteItem(Key.builder().partitionValue(firstname).build()))
        .map(CompletableFuture::join);
  }

  public Mono<Person> update(Person person) {
    return Mono.just(dynamoDbEnhancedAsyncClient.table(Person.TABLE_NAME,
            TableSchema.fromBean(Person.class)))
        .map(table -> table.updateItem(person))
        .map(CompletableFuture::join);
  }
}
