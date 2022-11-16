package com.matheus.springwebfluxdynamodbpoc.controller;

import com.matheus.springwebfluxdynamodbpoc.model.Book;
import com.matheus.springwebfluxdynamodbpoc.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/books")
public class BookResource {

  private final BookService bookService;

  public BookResource(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public Flux<Book> getAll() {
    return bookService.findAll();
  }

  @GetMapping("/isbn/{isbn}")
  public Mono<Book> findByIsbn(@PathVariable("isbn") final String isbn) {
    return bookService.findByIsbn(isbn);
  }

  @PostMapping
  public Mono<Book> add(final Book book) {
    return bookService.add(book);
  }
}
