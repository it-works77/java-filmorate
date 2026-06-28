package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {
    @Positive
    private int id; // целочисленный идентификатор

    @NotBlank(message = "логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    private String login; // логин пользователя

    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    @NonNull
    private String email; // электронная почта

    // имя для отображения может быть пустым — в таком случае будет использован логин;
    @NonNull
    private String name; // имя для отображения

    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday; // дата рождения

}
