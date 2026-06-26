package ru.yandex.practicum.filmorate.model;

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
    private String name; // название
    private String description; // описание
    // TODO или с TZ?
    private LocalDate releaseDate; // дата релиза
    private Duration duration; // продолжительность фильма

}
