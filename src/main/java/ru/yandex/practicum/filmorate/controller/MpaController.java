package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaRequestDto;
import ru.yandex.practicum.filmorate.dto.MpaResponseDto;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@Validated
@AllArgsConstructor
public class MpaController {
    private final FilmService filmService;

    @GetMapping
    public Collection<MpaResponseDto> getMpaAll() {
        log.info("Get all MPA ratings");
        return filmService.getMpaAll().stream()
                .map(FilmMapper::MpaRatingToMpaResponseDto)
                .toList();
    }

    @GetMapping("/{mpaId}")
    public MpaResponseDto getMpa(@PathVariable @Positive Integer mpaId) {
        log.info("Получаем MPA по id={}", mpaId);
        return FilmMapper.MpaRatingToMpaResponseDto(filmService.getMpa(mpaId));
    }
}
