package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final UniqueConstraint filmStorageUniqueConstraint = new UniqueConstraint();
    private int currentId = 1; // Максимальный занятый идентификатор. Освободившиеся не занимаем

    /**
     * @param film
     * @return
     */
    @Override
    public Film add(Film film) {
        if (!filmStorageUniqueConstraint.isInsertValid(film)) {
            log.warn("Фильм {} с датой релиза {} уже существует", film.getName(), film.getReleaseDate().toString());
            throw new EntityAlreadyExistsException("Фильм %s с датой релиза %s уже существует"
                    .formatted(film.getName(), film.getReleaseDate().toString()));
        }

        int id = getNextId();
        Film newFilm = Film.builder()
                .id(id)
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        films.put(id, newFilm);
        filmStorageUniqueConstraint.add(newFilm);
        return newFilm;
    }

    /**
     * @param newFilm
     * @return
     */
    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("При обновлении фильма не задан его id");
            throw new IllegalArgumentException("При обновлении фильма не задан его id");
        }

        if (!films.containsKey(newFilm.getId())) {
            log.warn("Не удалось обновить фильм. Нет фильма с id={}", newFilm.getId());
            throw new EntityNotFoundException("Не удалось обновить фильм. Нет фильма с id=%d"
                    .formatted(newFilm.getId()));
        }

        if (!filmStorageUniqueConstraint.isUpdateValid(newFilm)) {
            String warnMessage = "Фильм %s уже сохранен с другим Id=%d"
                    .formatted(newFilm.getName(), newFilm.getId());
            log.warn(warnMessage);
            throw new EntityAlreadyExistsException(warnMessage);
        }

        films.put(newFilm.getId(), newFilm);
        log.debug("Обновили фильм {}", newFilm);
        return newFilm;
    }

    /**
     * @param id фильма
     * @return фильм, если есть в хранилище
     */
    @Override
    public Optional<Film> get(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    /**
     * @return все фильмы в хранилище
     */
    @Override
    public Collection<Film> getAll() {
        return films.values().stream()
                .map(Film::of)
                .collect(Collectors.toList());
    }

    /**
     * @param id фильма
     * @return удаленный фильм или null, если таким id не было
     */
    @Override
    public Optional<Film> remove(Integer id) {
        Film film = films.get(id);
        if (film == null) {
            return Optional.empty();
        } else {
            filmStorageUniqueConstraint.remove(film);
            return Optional.of(films.remove(id));
        }
    }

    private Integer getNextId() {
        return currentId++;
    }

    private class UniqueConstraint {
        Map<String, Integer> constraints = new HashMap<>();

        void add(Film film) {
            constraints.put(getConstraintKey(film), film.getId());
        }

        boolean isInsertValid(Film film) {
            return !constraints.containsKey(getConstraintKey(film));
        }

        boolean isUpdateValid(Film film) {
            Integer constraintId = constraints.get(getConstraintKey(film));
            if (constraintId == null) {
                // Ограничения нет в constraints. Можно обновлять
                return true;
            }
            // Ограничение есть, но оно сохранено для обновляемого объекта. Можно обновлять
            return constraintId.equals(film.getId());
        }

        boolean remove(Film film) {
            Integer removedUserId = constraints.remove(getConstraintKey(film));
            return removedUserId != null;
        }

        private String getConstraintKey(Film film) {
            return film.getName() + film.getReleaseDate().toString();
        }
    }
}
