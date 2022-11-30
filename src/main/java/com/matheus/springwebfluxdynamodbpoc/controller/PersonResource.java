package com.matheus.springwebfluxdynamodbpoc.controller;

import com.matheus.springwebfluxdynamodbpoc.model.Person;
import com.matheus.springwebfluxdynamodbpoc.service.PersonService;
import com.matheus.springwebfluxdynamodbpoc.vo.request.PaginationRequest;
import com.matheus.springwebfluxdynamodbpoc.vo.response.PaginationResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person")
public class PersonResource {

  private final PersonService personService;

  public PersonResource(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping
  public Flux<Person> getAll() {
    return personService.findAll();
  }

  @GetMapping("/firstname/{firstname}")
  public Mono<PaginationResponse<Person>> findByFirstName(
      @PathVariable("firstname") final String firstname,
      PaginationRequest paginationRequest) {
    return personService.findByFirstName(firstname, paginationRequest);
  }

  @GetMapping("/firstname/{firstname}/lastname/{lastname}")
  public Mono<Person> findByIsbnAndName(@PathVariable("firstname") final String firstname,
      @PathVariable("lastname") final String lastname) {
    return personService.findByFirstNameAndLastName(firstname, lastname);
  }

  @GetMapping("/cpf/{cpf}")
  public Mono<PaginationResponse<Person>> findByCpf(
      @PathVariable("cpf") String cpf,
      PaginationRequest paginationRequest) {
    return personService.findByCpf(cpf, paginationRequest);
  }

  @PostMapping
  public Mono<Person> add(@RequestBody final Person person) {
    return personService.add(person);
  }

  @DeleteMapping("/{firstname}")
  public Mono<Person> delete(@PathVariable("firstname") String firstname) {
    return personService.delete(firstname);
  }

  @PutMapping
  public Mono<Person> update(@RequestBody Person person) {
    return personService.update(person);
  }
}
