package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.NotBefore;

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

    private Set<GenreDto> genres; // жанры фильма

    private MpaDto mpa; // возрастной рейтинг
}
