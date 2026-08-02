package ru.yandex.practicum.filmorate.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MpaRating {
    G(1, "G"),       // Нет возрастных ограничений
    PG(2, "PG"),      // Детям рекомендуется смотреть фильм с родителями
    PG_13(3, "PG-13"),   // Детям до 13 лет просмотр не желателен
    R(4, "R"),       // Лицам до 17 лет просматривать фильм можно только в присутствии взрослого
    NC_17(5, "NC-17");    // Лицам до 18 лет просмотр запрещён

    private final int code;
    private final String displayName;

    MpaRating(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static MpaRating byCode(int code) {
        return Arrays.stream(values())
                .filter(rating -> rating.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный код MPA рейтинга: " + code));
    }

}
