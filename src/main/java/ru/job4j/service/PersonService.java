package ru.job4j.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
@Slf4j
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private BCryptPasswordEncoder encoder;

    public Optional<Person> save(Person person) {
        Optional<Person> optionalPerson = Optional.empty();
        person.setPassword(encoder.encode(person.getPassword()));
        try {
            optionalPerson = Optional.of(personRepository.save(person));
        } catch (DataIntegrityViolationException e) {
            log.error("Пользователь с этим login уже существует", e);
        }
        return optionalPerson;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public boolean delete(int id) {
        var personOptional = personRepository.findById(id);
        return personOptional.filter(person -> this.delete(person.getId())).isPresent();
    }

    public Optional<Person> findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    public boolean update(Person person) {
        var personOptional = personRepository.findById(person.getId());
        if (personOptional.isPresent()) {
            Person result = personOptional.get();
            if (person.getLogin() != null) {
                result.setLogin(person.getLogin());
            }
            result.setPassword(encoder.encode(person.getPassword()));
            this.save(result);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var personOptional = personRepository.findByLogin(login);
        if (personOptional.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        return new User(
                personOptional.get().getLogin(),
                personOptional.get().getPassword(),
                emptyList());
    }
}
