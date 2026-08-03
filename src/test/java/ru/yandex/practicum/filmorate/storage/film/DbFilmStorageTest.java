package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.enums.MpaRating;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate.storage")
class DbFilmStorageTest {
    @Autowired
    @Qualifier("dbFilmStorage")
    private DbFilmStorage dbFilmStorage;

    private Film film1;
    private Film film2;

    @BeforeEach
    void setUp() {
        film1 = Film.builder()
                .name("Asdf")
                .description("Desc")
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(100)
                .mpa(MpaRating.PG_13)
                .build();

        film2 = Film.builder()
                .name("Qwer")
                .description("Desc")
                .releaseDate(LocalDate.of(2002,1,1))
                .duration(50)
                .mpa(MpaRating.PG)
                .build();
    }

    @Test
    void addFilms_WhenNotExists_getOk() {
        dbFilmStorage.add(film1);
        dbFilmStorage.add(film2);
    }

    @Test
    void addFilm_WhenSameNameAndReleaseDate_getDuplicateKeyException() {
        dbFilmStorage.add(film1);
        // CONSTRAINT unique_film_name_release UNIQUE (name, release_date)
        assertThrows(DuplicateKeyException.class, () -> dbFilmStorage.add(film1));
    }

    @Test
    void updateFilm_WhenIdExists_getOk() {
        Film filmForUpdate = dbFilmStorage.add(film1);
        film1.setName("NewName");
        dbFilmStorage.update(film1);
        Optional<Film> updatedFilmOpt = dbFilmStorage.get(filmForUpdate.getId());
        assertTrue(film1.equals(updatedFilmOpt.get()));
    }

    @Test
    void updateFilm_WhenIdNotExists_getEntityNotFoundException() {
        dbFilmStorage.add(film1);
        film1.setId(999);
        assertThrows(EntityNotFoundException.class, () -> dbFilmStorage.update(film1));
    }

    @Test
    void updateFilm_WhenSameNameAndReleaseDate_getDuplicateKeyException() {
        // CONSTRAINT unique_film_name_release UNIQUE (name, release_date)
        Film filmForUpdate = dbFilmStorage.add(film1);
        dbFilmStorage.add(film2);
        // Аналогичный film2, но id от film1
        Film film = Film.builder()
                .id(filmForUpdate.getId())
                .name("Qwer")
                .description("Desc1")
                .releaseDate(LocalDate.of(2002,1,1))
                .duration(100)
                .mpa(MpaRating.R)
                .build();
        assertThrows(DuplicateKeyException.class, () -> dbFilmStorage.update(film));
    }

    @Test
    void getFilmById_WhenIdExists_getFilm() {
        Film addedFilm = dbFilmStorage.add(film1);
        Optional<Film> filmOpt = dbFilmStorage.get(addedFilm.getId());
        assertTrue(film1.equals(filmOpt.get()));
    }

    @Test
    void getFilmById_WhenIdNotExists_getEmpty() {
        dbFilmStorage.add(film1);
        Optional<Film> filmOpt = dbFilmStorage.get(999);
        assertTrue(filmOpt.isEmpty());
    }


    @Test
    void getAll() {
        dbFilmStorage.add(film1);
        dbFilmStorage.add(film2);
        assertEquals(2, dbFilmStorage.getAll().size());
    }

    @Test
    void getAll_whenDbIsEmpty() {
        Collection<Film> films = dbFilmStorage.getAll();
        assertEquals(0, films.size());
    }

    @Test
    void remove() {
        dbFilmStorage.add(film1);
        Film filmToRemove = dbFilmStorage.add(film2);
        dbFilmStorage.remove(filmToRemove.getId());
        assertEquals(1, dbFilmStorage.getAll().size());
    }
}