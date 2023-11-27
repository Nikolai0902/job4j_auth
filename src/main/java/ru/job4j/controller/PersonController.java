package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Person;
import ru.job4j.model.PersonDTO;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/person")
public class PersonController {

    private final PersonService persons;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        if (person.isPresent()) {
            return new ResponseEntity<Person>(
                    person.orElse(new Person()),
                    HttpStatus.OK
            );
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person is not found.");
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        var result = this.persons.save(person);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с этим login уже существует");
        }
        return new ResponseEntity<>(
                result.get(), HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Boolean> update(@Valid @RequestBody Person person) {
        if (this.persons.update(person)) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Обьект не обновлен!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (this.persons.delete(id)) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не удален!");
    }

    @PatchMapping("/")
    public ResponseEntity<Void> patch(@Valid @RequestBody PersonDTO personDto) {
        Person updatePerson = new Person();
        updatePerson.setId(personDto.getId());
        updatePerson.setPassword(personDto.getPassword());
        return new ResponseEntity<>(this.persons.update(updatePerson) ? HttpStatus.OK : HttpStatus.CONFLICT);
    }

    /**
     * Используется если выброс очень специфичного исключения;
     * @param e сообщение специфического исключения
     * @param request запос
     * @param response ответ
     * @throws IOException
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        log.error(e.getLocalizedMessage());
    }
}
