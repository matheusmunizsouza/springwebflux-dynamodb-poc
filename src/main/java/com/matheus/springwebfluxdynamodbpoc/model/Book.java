package com.matheus.springwebfluxdynamodbpoc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Objects;

@DynamoDbBean
public final class Book {

  public static final String TABLE_NAME = "book";
  private String isbn;
  private String name;
  private String description;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("isbn")
  public String getIsbn() {
    return isbn;
  }

  @DynamoDbAttribute("name")
  @JsonInclude(Include.NON_NULL)
  public String getName() {
    return name;
  }

  @DynamoDbAttribute("description")
  @JsonInclude(Include.NON_NULL)
  public String getDescription() {
    return description;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return isbn.equals(book.isbn) && name.equals(book.name) && Objects.equals(description,
        book.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isbn, name, description);
  }

  @Override
  public String toString() {
    return "Book{" +
        "isbn='" + isbn + '\'' +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
