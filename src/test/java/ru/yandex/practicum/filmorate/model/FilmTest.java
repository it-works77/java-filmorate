package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private static final int MIN_FILM_YEAR = 1895;
    private static final int MIN_FILM_MONTH = 12;
    private static final int MIN_FILM_DAY = 28;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void createFilm_whenCorrectParams_getOk() {
        assertDoesNotThrow(() -> {
                    Film.builder()
                            .id(1)
                            .name("asdf")
                            .description("ASDF")
                            .releaseDate(LocalDate.now())
                            .duration(1)
                            .build();
                }
        );
    }

    @Test
    void createFilm_whenNameIsBlank_getValidationException() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("ASDF")
                .releaseDate(LocalDate.now())
                .duration(1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    void createFilm_whenDateIsAfterMinDate_getOk() {
        Film film = Film.builder()
                .id(1)
                .name("asdf")
                .description("ASDF")
                .releaseDate(LocalDate.of(MIN_FILM_YEAR, MIN_FILM_MONTH, MIN_FILM_DAY + 1))
                .duration(1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createFilm_whenDateIsEqualMinDate_getOk() {
        Film film = Film.builder()
                .id(1)
                .name("asdf")
                .description("ASDF")
                .releaseDate(LocalDate.of(MIN_FILM_YEAR, MIN_FILM_MONTH, MIN_FILM_DAY))
                .duration(1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createFilm_whenDateIsBeforeMinDate_getValidationError() {
        Film film = Film.builder()
                .id(1)
                .name("asdf")
                .description("ASDF")
                .releaseDate(LocalDate.of(MIN_FILM_YEAR, MIN_FILM_MONTH, MIN_FILM_DAY - 1))
                .duration(1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("releaseDate", violation.getPropertyPath().toString());
        assertEquals("Дата релиза не может быть раньше 28.12.1895", violation.getMessage());
    }

    @Test
    void checkEquals_whenEqualExcludingId_getEqual() {
        Film film1 = Film.builder()
                .id(1)
                .name("asdf")
                .description("ASDF")
                .releaseDate(LocalDate.now())
                .duration(1)
                .build();

        Film film2 = Film.builder()
                .id(2)
                .name("asdf")
                .description("ASDF")
                .releaseDate(LocalDate.now())
                .duration(1)
                .build();
        assertEquals(film1, film2);
    }
}