package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.storage.film.DbFilmStorageQueries.*;

@Slf4j
@Repository
@Qualifier("dbFilmStorage")
public class DbFilmStorage extends BaseDbStorage<Film> implements FilmStorage {

    public DbFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
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
        return newFilm;
    }

    @Override
    public Optional<Film> get(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public boolean remove(Integer id) {
        return delete(DELETE_BY_ID_QUERY, id);
    }

}
