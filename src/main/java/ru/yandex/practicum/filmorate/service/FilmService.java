package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.config.AppConfig;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;
    private final AppConfig appConfig;

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film newFilm) {
        Film updatedFilm = filmStorage.update(newFilm);
        log.debug("Обновили фильм {}", updatedFilm);
        return updatedFilm;
    }

    public Optional<Film> get(Integer filmId) {
        return filmStorage.get(filmId);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(Integer filmId, Integer userId) {
        checkUserAndFilmExistence(filmId, userId);
        likeStorage.addLike(filmId, userId);
        log.info("Пользователь {} добавил лайк к фильму {}", userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        checkUserAndFilmExistence(filmId, userId);
        likeStorage.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
    }

    public Collection<Film> getTopFilmsByLikes() {
        return getTopFilmsByLikes(appConfig.getTopByLikesFilmsNumber());
    }

    public Collection<Film> getTopFilmsByLikes(Integer topFilmsNumber) {
        if (topFilmsNumber < 1) {
            throw new IllegalArgumentException("Количество популярных фильмов в запросе должно быть больше нуля");
        }

        Map<Integer, HashSet<Integer>> likes = likeStorage.getAllLikes();
        List<Integer> filmIds = likes.entrySet().stream()
                .sorted((e1, e2) ->
                        Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(topFilmsNumber)
                .map(Map.Entry::getKey)
                .toList();

        log.debug("sorted filmIds = {}", filmIds);

        return filmIds.stream()
                .map(filmStorage::get)
                .map(filmOpt -> filmOpt.orElseThrow(() ->
                        new IllegalStateException("Неконсистентное состояние likeStorage" +
                                " и filmStorage: не найден фильм по Id")))
                .toList();
    }

    private void checkUserAndFilmExistence(Integer filmId, Integer userId) {
        if (filmStorage.get(filmId).isEmpty()) {
            String msg = "Нет фильма с id=%d".formatted(filmId);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }
        if (userService.get(userId).isEmpty()) {
            String msg = "Нет пользователя с id=%d".formatted(filmId);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }
    }
}
