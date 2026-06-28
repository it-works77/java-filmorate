package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    /*
     * добавление фильма;
     * */
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Create film: {}", film);
        Film result = filmService.add(film);
        log.info("Film created: {}", result);
        return result;

    }

    /*
     * обновление фильма;
     * */
    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Update film: {}", newFilm);
        Film result = filmService.update(newFilm);
        log.info("Film updated: {}", newFilm);
        return result;
    }

    /*
     * получение всех фильмов.
     */
    @GetMapping
    public Collection<Film> getAll() {
        log.info("Get all films");
        return filmService.getAll();
    }
}
