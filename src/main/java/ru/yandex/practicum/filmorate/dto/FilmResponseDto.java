package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class FilmResponseDto {
    private Integer id; // целочисленный идентификатор

    private String name; // название

    private String description; // описание

    private LocalDate releaseDate; // дата релиза

    private int duration; // продолжительность фильма в минутах

    private Set<GenreRequestDto> genres; // жанры фильма

    private MpaRequestDto mpa; // возрастной рейтинг
}
