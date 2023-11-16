package ru.job4j.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private BCryptPasswordEncoder encoder;

    public Optional<Person> save(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        Person result = personRepository.save(person);
        return Optional.of(result);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public boolean delete(int id) {
        Person person = new Person();
        person.setId(id);
        personRepository.delete(person);
        return personRepository.findById(person.getId()).isEmpty();
    }

    public Optional<Person> findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    public boolean update(Person person) {
        var personOptional = personRepository.findById(person.getId());
        if (personOptional.isPresent()) {
            Person result = personOptional.get();
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
