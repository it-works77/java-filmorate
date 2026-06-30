package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.Collection;

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
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        log.info("Create film: {}", film);
        Film result = filmService.add(film);
        log.info("Film created: {}", result);
        return result;

    }

    /*
     * обновление фильма;
     * */
    @PutMapping
    public Film update(@Validated(Update.class) @RequestBody Film newFilm) {
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
