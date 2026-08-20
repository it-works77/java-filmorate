package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilmGenre {
    private Integer filmId;
    private String  genre;

    public FilmGenre(String genre) {
        this.genre = genre;
    }
}
