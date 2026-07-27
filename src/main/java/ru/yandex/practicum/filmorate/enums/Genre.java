package ru.yandex.practicum.filmorate.enums;

import lombok.Getter;

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
}
