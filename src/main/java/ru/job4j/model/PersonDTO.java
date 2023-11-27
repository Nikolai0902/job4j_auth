package ru.job4j.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {

    @NotNull(message = "Id for person must be non null")
    private int id;

    @NotBlank(message = "New Password must be not empty")
    @Size(min = 3, max = 200, message
            = "Login must be between 3 and 200 characters")
    private String password;
}
