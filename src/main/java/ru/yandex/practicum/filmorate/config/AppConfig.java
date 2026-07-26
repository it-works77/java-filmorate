package ru.yandex.practicum.filmorate.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties("filmorate")
public class AppConfig {
    private final int topByLikesFilmsNumber; // Какое количество популярных фильмов выводить, если не задано
}
