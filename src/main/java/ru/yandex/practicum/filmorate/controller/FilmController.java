package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    /*
     * добавление фильма;
     * */
    @PostMapping
    public Film create(@RequestBody Film film) {
        // TODO
        return film;
    }

    /*
     * обновление фильма;
     * */
    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        // TODO
        return newFilm;
    }

    /*
     * получение всех фильмов.
     */
    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }
}
