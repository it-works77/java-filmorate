package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */

@Data
@Builder
public class Film {
    private int id; // целочисленный идентификатор
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name; // название
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description; // описание
    // TODO или с TZ?
    // TODO Validation дата релиза — не раньше 28 декабря 1895 года;
    private LocalDate releaseDate; // дата релиза
    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом")
    private int duration; // продолжительность фильма в минутах

}
