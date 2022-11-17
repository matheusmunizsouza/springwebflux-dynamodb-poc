package com.matheus.springwebfluxdynamodbpoc.controller;

import com.matheus.springwebfluxdynamodbpoc.model.Book;
import com.matheus.springwebfluxdynamodbpoc.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/isbn/{isbn}/name/{name}")
  public Flux<Book> findByIsbnAndName(@PathVariable("isbn") final String isbn,
      @PathVariable("name") final String name) {
    return bookService.findByIsbnAndName(isbn, name);
  }

  @PostMapping
  public Mono<Book> add(@RequestBody final Book book) {
    return bookService.add(book);
  }
}
