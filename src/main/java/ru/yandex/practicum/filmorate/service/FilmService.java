package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    /*
     * - название не может быть пустым;
     * - максимальная длина описания — 200 символов;
     * - дата релиза — не раньше 28 декабря 1895 года;
     * - продолжительность фильма должна быть положительным числом.
     */

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film add(Film film) {
        Optional<Film> oldFilm = getFilm(film);
        if (oldFilm.isPresent()) {
            log.warn("Фильм {} уже существует", film.getName());
            throw new EntityAlreadyExistsException("Фильм %s уже существует".formatted(film.getName()));
        }

        int id = getId();
        Film newFilm = Film.builder()
                .id(id)
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        films.put(id, newFilm);
        return newFilm;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("При обновлении фильма не задан его id");
            throw new IllegalArgumentException("При обновлении фильма не задан его id");
        }

        Optional<Film> oldFilm = getFilm(newFilm);
        if (oldFilm.isPresent()) {
            if (oldFilm.get().getId()
                    .equals(newFilm.getId())) {
                log.debug("Такой фильм уже сохранен с тем же самым Id {}", newFilm);
                return newFilm;
            } else {
                String warnMessage = "Фильм %s уже сохранен с другим Id=%d"
                        .formatted(newFilm.getName(), oldFilm.get().getId());
                log.warn(warnMessage);
                throw new EntityAlreadyExistsException(warnMessage);
            }
        }

        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.debug("Обновили фильм {}", newFilm);
            return newFilm;
        } else {
            log.warn("Не удалось обновить фильм. Нет такого id={}", newFilm.getId());
            throw new IllegalArgumentException("Нет фильма с id=%d".formatted(newFilm.getId()));
        }
    }

    private Optional<Film> getFilm(Film film) {
        return films.values().stream()
                .filter(film::equals)
                .findFirst();
    }

    private Integer getId() {
        Optional<Integer> currentMaxId = films.keySet().stream()
                .max(Integer::compareTo);

        return currentMaxId.map(id -> id + 1).orElse(1);
    }
}
