package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.enums.MpaRating;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */

@Data
@Builder
public class Film {
    /*
     * - название не может быть пустым;
     * - максимальная длина описания — 200 символов;
     * - дата релиза — не раньше 28 декабря 1895 года;
     * - продолжительность фильма должна быть положительным числом.
     */
    @EqualsAndHashCode.Exclude
    private Integer id; // целочисленный идентификатор

    private String name; // название

    private String description; // описание

    private LocalDate releaseDate; // дата релиза

    private int duration; // продолжительность фильма в минутах

    @Builder.Default
    private Set<Genre> genres = new HashSet<>(); // жанры фильма

    private MpaRating mpa; // возрастной рейтинг

    private Set<Integer> userLikes; // идентификаторы пользователей, поставивших лайк фильму

    public static Film of(Film film) {
        return Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .mpa(film.getMpa())
                .build();
    }
}
