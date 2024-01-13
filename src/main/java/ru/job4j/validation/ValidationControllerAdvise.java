package ru.job4j.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Если валидация не проходит, то в контексте приложения кидается исключение MethodArgumentNotValidException,
 * поэтому для того чтобы возвращать понятные ответы клиенту нужно его отлавливать.
 * Способов - @ControllerAdivise + @ExceptionHandler.
 *
 * MethodArgumentNotValidException возникает только если @Valid используется для аргумента метода.
 */
@ControllerAdvice
public class ValidationControllerAdvise {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }

}

