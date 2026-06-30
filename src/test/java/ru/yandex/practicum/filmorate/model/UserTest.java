package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.Create;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void createUser_whenBirthdayIsInPast_getOk() {
        User user = User.builder()
                .login("asdf")
                .email("test@test.lz")
                .name("asdf")
                .birthday(LocalDate.now().minusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createFilm_whenDateIsToday_getOk() {
        User user = User.builder()
                .login("asdf")
                .email("test@test.lz")
                .name("asdf")
                .birthday(LocalDate.now())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createUser_whenDateIsInFuture_getValidationError() {
        User user = User.builder()
                .login("asdf")
                .email("test@test.lz")
                .name("asdf")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("birthday", violation.getPropertyPath().toString());
        assertEquals("дата рождения не может быть в будущем", violation.getMessage());
    }
}