package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.NotBefore;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
@Builder
public class Film {
    @Positive
    @EqualsAndHashCode.Exclude
    private Integer id; // целочисленный идентификатор

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name; // название

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    @NonNull
    private String description; // описание

    @NotBefore(value = "1895-12-28", message = "Дата релиза не может быть раньше 28.12.1895")
    @NonNull
    private LocalDate releaseDate; // дата релиза

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration; // продолжительность фильма в минутах

}
