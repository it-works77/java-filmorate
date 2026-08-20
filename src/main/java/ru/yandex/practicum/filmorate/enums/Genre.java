package ru.yandex.practicum.filmorate.enums;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

import java.util.Arrays;

@Getter
public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private final int code;
    private final String displayName;

    Genre(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static Genre byCode(int code) {
        return Arrays.stream(values())
                .filter(genre -> genre.code == code)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Неизвестный код жанра: " + code));
    }
}
