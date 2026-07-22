package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@AllArgsConstructor
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
     * получение фильма по id
     */
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable @Positive Integer id) {
        log.info("Получаем фильм по id={}", id);
        return filmService.get(id);
    }

    /*
     * получение всех фильмов.
     */
    @GetMapping
    public Collection<Film> getAll() {
        log.info("Get all films");
        return filmService.getAll();
    }

    /*
     * пользователь ставит лайк фильму
     * */
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable @Positive Integer id,
                        @PathVariable @Positive Integer userId) {
        log.info("Пользователь c id={} ставит лайк фильму c id={}", id, userId);
        filmService.addLike(id, userId);
    }

    /*
     * пользователь удаляет лайк
     * */
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable @Positive Integer id,
                           @PathVariable @Positive Integer userId) {
        log.info("Пользователь c id={} удаляет лайк у фильма c id={}", id, userId);
        filmService.removeLike(id, userId);
    }

    /*
     * возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10
     * */
    @GetMapping("/popular")
    public Collection<Film> addLike(@RequestParam(name = "count", required = false) @Positive Integer topByLikesFilmsNumber) {
        log.info("Возвращаем {} популярных фильмов", topByLikesFilmsNumber);
        if (topByLikesFilmsNumber == null) {
            return filmService.getTopFilmsByLikes();
        } else {
            return filmService.getTopFilmsByLikes(topByLikesFilmsNumber);
        }
    }
}
