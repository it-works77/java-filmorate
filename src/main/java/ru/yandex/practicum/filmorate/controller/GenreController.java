package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreResponseDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@Validated
@AllArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    public Collection<GenreResponseDto> getMpaAll() {
        log.info("Get all genres ratings");
        return filmService.getGenreAll();
    }

    @GetMapping("/{genreId}")
    public GenreResponseDto getMpa(@PathVariable @Positive Integer genreId) {
        log.info("Получаем жанр по id={}", genreId);
        return filmService.getGenre(genreId);
    }

}
