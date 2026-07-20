package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    /*
     * - название не может быть пустым;
     * - максимальная длина описания — 200 символов;
     * - дата релиза — не раньше 28 декабря 1895 года;
     * - продолжительность фильма должна быть положительным числом.
     */

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film newFilm) {
        Film updatedFilm = filmStorage.update(newFilm);
        log.debug("Обновили фильм {}", updatedFilm);
        return updatedFilm;

    }

}
