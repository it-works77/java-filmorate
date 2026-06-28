package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.annotation.NotBefore;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */

@Data
@Builder
public class Film {
    private final LocalDate FIRST_FILM_DATE = LocalDate.of(1875, 12, 28);
    @Positive
    private int id; // целочисленный идентификатор

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name; // название

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    @NonNull
    private String description; // описание

    @NotBefore(value = "1875-12-28", message = "Дата релиза не может быть раньше 28.12.1875")
    @NonNull
    private LocalDate releaseDate; // дата релиза

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration; // продолжительность фильма в минутах

}
