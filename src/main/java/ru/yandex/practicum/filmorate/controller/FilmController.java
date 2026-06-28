package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
    public Film create(@RequestBody Film film) {
        // TODO
        /* Добавьте логирование для операций, которые изменяют сущности — добавляют и обновляют их.
         * Также логируйте причины ошибок — например, если валидация не пройдена.
         * */
        log.info("Create film {}", film);
        return film;
    }

    /*
     * обновление фильма;
     * */
    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        // TODO
        /* Добавьте логирование для операций, которые изменяют сущности — добавляют и обновляют их.
         * Также логируйте причины ошибок — например, если валидация не пройдена.
         * */
        log.info("Update film {}", newFilm);
        return newFilm;
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
