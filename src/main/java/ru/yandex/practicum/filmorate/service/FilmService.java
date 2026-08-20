package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.config.AppConfig;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.GenreResponseDto;
import ru.yandex.practicum.filmorate.dto.MpaResponseDto;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.enums.MpaRating;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;
    private final AppConfig appConfig;

    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage,
                       @Qualifier("dbLikeStorage") LikeStorage likeStorage,
                       UserService userService,
                       AppConfig appConfig) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.userService = userService;
        this.appConfig = appConfig;
    }

    public FilmResponseDto add(FilmRequestDto filmRequestDto) {
        Film film = FilmMapper.mapFilmRequestDtoToFilm(filmRequestDto);
        return FilmMapper.mapFilmToFilmResponseDto(filmStorage.add(film));
    }

    public FilmResponseDto update(FilmRequestDto filmRequestDto) {
        Film newFilm = FilmMapper.mapFilmRequestDtoToFilm(filmRequestDto);
        checkFilmExistence(newFilm.getId());
        Film updatedFilm = filmStorage.update(newFilm);
        log.debug("Обновили фильм {}", updatedFilm);
        return FilmMapper.mapFilmToFilmResponseDto(updatedFilm);
    }

    public FilmResponseDto get(Integer filmId) {
        Film film = filmStorage.get(filmId).orElseThrow(() ->
                new EntityNotFoundException("Не найден фильм с id=" + filmId));
        return FilmMapper.mapFilmToFilmResponseDto(film);
    }

    public Collection<FilmResponseDto> getAll() {
        return filmStorage.getAll().stream()
                .map(FilmMapper::mapFilmToFilmResponseDto)
                .toList();
    }

    public void addLike(Integer filmId, Integer userId) {
        checkUserExistence(userId);
        checkFilmExistence(filmId);
        likeStorage.addLike(filmId, userId);
        log.info("Пользователь {} добавил лайк к фильму {}", userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        checkUserExistence(userId);
        checkFilmExistence(filmId);
        likeStorage.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
    }

    public Collection<FilmResponseDto> getTopFilmsByLikes() {
        return getTopFilmsByLikes(appConfig.getTopByLikesFilmsNumber());
    }

    public Collection<FilmResponseDto> getTopFilmsByLikes(Integer topFilmsNumber) {
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

        List<Film> films = filmIds.stream()
                .map(filmStorage::get)
                .map(filmOpt -> filmOpt.orElseThrow(() ->
                        new IllegalStateException("Неконсистентное состояние likeStorage" +
                                " и filmStorage: не найден фильм по Id")))
                .toList();

        return films.stream()
                .map(FilmMapper::mapFilmToFilmResponseDto)
                .toList();
    }

    public MpaResponseDto getMpa(Integer id) {
        return FilmMapper.mpaRatingToMpaResponseDto(MpaRating.byCode(id));
    }

    public Collection<MpaResponseDto> getMpaAll() {
        return Arrays.stream(MpaRating.values())
                .map(FilmMapper::mpaRatingToMpaResponseDto)
                .toList();
    }

    public GenreResponseDto getGenre(Integer id) {
        return FilmMapper.genreToGenreResponseDto(Genre.byCode(id));
    }

    public Collection<GenreResponseDto> getGenreAll() {
        return Arrays.stream(Genre.values())
                .map(FilmMapper::genreToGenreResponseDto)
                .toList();
    }

    private void checkFilmExistence(Integer filmId) {
        if (filmStorage.get(filmId).isEmpty()) {
            String msg = "Нет фильма с id=%d".formatted(filmId);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }
    }

    private void checkUserExistence(Integer userId) {
        try {
            userService.get(userId);
        } catch (RuntimeException e) {
            String msg = "Нет пользователя с id=%d".formatted(userId);
            log.warn(msg);
            throw e;
        }
    }
}
