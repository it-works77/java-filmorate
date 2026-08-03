package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.film.DbFilmStorageQueries.*;

@Slf4j
@Repository
@Qualifier("dbFilmStorage")
public class DbFilmStorage extends BaseDbStorage<Film> implements FilmStorage {

    public DbFilmStorage(JdbcTemplate jdbc, RowMapper<Film> filmRowMapper) {
        super(jdbc, filmRowMapper);
    }

    @Override
    public Film add(Film film) {
        int id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().name());
        film.setId(id);

        Set<Genre> filmGenres = film.getGenres();
        if (!filmGenres.isEmpty()) {
            filmGenres.stream()
                    .forEach(genre -> insert(INSERT_GENRE_QUERY,
                            film.getId(), genre.name()));
        }
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        Optional<Film> film = get(newFilm.getId());
        if (film.isEmpty()) {
            throw new EntityNotFoundException("Не найден фильм с id=" + newFilm.getId());
        }

        int rowsUpdated = update(UPDATE_BY_ID_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().name(),
                newFilm.getId());

        if (rowsUpdated != 1) {
            String msg = "Количество обновленных фильмов по id=%d не равно 1!".formatted(newFilm.getId());
            log.debug(msg);
            throw new InternalServerException(msg);
        }

        Set<Genre> filmGenres = newFilm.getGenres();
        if (!filmGenres.isEmpty()) {
            filmGenres.stream()
                    .forEach(genre -> update(DELETE_FILM_GENRES_BY_ID_QUERY,
                            newFilm.getId()));

            filmGenres.stream()
                    .forEach(genre -> insert(INSERT_GENRE_QUERY,
                            newFilm.getId(), genre.name()));
        }

        return newFilm;
    }

    @Override
    public Optional<Film> get(Integer id) {
        Optional<Film> filmOpt = findOne(FIND_BY_ID_QUERY, id);
        filmOpt.ifPresent(film -> film.setGenres(getFilmGenres(id, film)));
        return filmOpt;
    }

    @Override
    public Collection<Film> getAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        films.stream().forEach(film -> film.setGenres(getFilmGenres(film.getId(), film)));
        return films;
    }

    @Override
    public boolean remove(Integer id) {
        return delete(DELETE_BY_ID_QUERY, id);
    }

    private Set<Genre> getFilmGenres(Integer id, Film film) {
        List<FilmGenre> filmGenres = jdbc.query(FIND_FILM_GENRES_BY_ID_QUERY, new FilmGenreRowMapper(), id);
        return filmGenres.stream()
                .map(filmGenre -> Genre.valueOf(filmGenre.getGenre()))
                .collect(Collectors.toSet());
    }

}
