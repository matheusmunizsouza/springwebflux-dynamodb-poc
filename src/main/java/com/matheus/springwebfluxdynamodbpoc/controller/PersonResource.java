package com.matheus.springwebfluxdynamodbpoc.controller;

import com.matheus.springwebfluxdynamodbpoc.model.Person;
import com.matheus.springwebfluxdynamodbpoc.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  public Flux<Person> findByFirstName(@PathVariable("firstname") final String firstname) {
    return personService.findByFirstName(firstname);
  }

  @GetMapping("/firstname/{firstname}/lastname/{lastname}")
  public Mono<Person> findByIsbnAndName(@PathVariable("firstname") final String isbn,
      @PathVariable("lastname") final String name) {
    return personService.findByFirstNameAndLastName(isbn, name);
  }

  @GetMapping("/cpf/{cpf}")
  public Flux<Person> findByCpf(@PathVariable("cpf") String cpf) {
    return personService.findByCpf(cpf);
  }

  @PostMapping
  public Mono<Person> add(@RequestBody final Person person) {
    return personService.add(person);
  }
}
