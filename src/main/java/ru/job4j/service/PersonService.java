package ru.job4j.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
@Slf4j
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;

    public Person save(Person person) {
        var personFind = personRepository.findById(person.getId());
        return personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public Optional<Person> findByLogin(String login) {
        return personRepository.findByLogin(login);
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
