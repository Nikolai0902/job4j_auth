package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();

    Optional<Person> findByLogin(String login);
}
