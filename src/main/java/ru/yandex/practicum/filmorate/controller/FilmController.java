package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    /*
     * добавление фильма;
     * */
    @PostMapping
    public FilmResponseDto create(@Validated(Create.class)  @RequestBody FilmRequestDto filmRequestDto) {
        log.info("Create film: {}", filmRequestDto);
        Film result = filmService.add(FilmMapper.mapFilmRequestDtoToFilm(filmRequestDto));
        log.info("Film created: {}", result);
        return FilmMapper.mapFilmToFilmResponseDto(result);
    }

    /*
     * обновление фильма;
     * */
    @PutMapping
    public FilmResponseDto update(@Validated(Update.class) @RequestBody FilmRequestDto filmRequestDto) {
        log.info("Update film: {}", filmRequestDto);
        Film result = filmService.update(FilmMapper.mapFilmRequestDtoToFilm(filmRequestDto));
        log.info("Film updated: {}", result);
        return FilmMapper.mapFilmToFilmResponseDto(result);
    }

    /*
     * получение фильма по id
     */
    @GetMapping("/{id}")
    public FilmResponseDto getFilm(@PathVariable @Positive Integer id) {
        log.info("Получаем фильм по id={}", id);
        return FilmMapper.mapFilmToFilmResponseDto(filmService.get(id));
    }

    /*
     * получение всех фильмов.
     */
    @GetMapping
    public Collection<FilmResponseDto> getAll() {
        log.info("Get all films");
        return filmService.getAll().stream()
                .map(FilmMapper::mapFilmToFilmResponseDto)
                .toList();
    }

    /*
     * пользователь ставит лайк фильму
     * */
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable @Positive Integer id,
                        @PathVariable @Positive Integer userId) {
        log.info("Пользователь c id={} ставит лайк фильму c id={}", id, userId);
        filmService.addLike(id, userId);
    }

    /*
     * пользователь удаляет лайк
     * */
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable @Positive Integer id,
                           @PathVariable @Positive Integer userId) {
        log.info("Пользователь c id={} удаляет лайк у фильма c id={}", id, userId);
        filmService.removeLike(id, userId);
    }

    /*
     * возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10
     * */
    @GetMapping("/popular")
    public Collection<FilmResponseDto> addLike(@RequestParam(name = "count", required = false) @Positive Integer topByLikesFilmsNumber) {
        log.info("Возвращаем {} популярных фильмов", topByLikesFilmsNumber);
        if (topByLikesFilmsNumber == null) {
            return filmService.getTopFilmsByLikes().stream()
                    .map(FilmMapper::mapFilmToFilmResponseDto)
                    .toList();
        } else {
            return filmService.getTopFilmsByLikes(topByLikesFilmsNumber).stream()
                    .map(FilmMapper::mapFilmToFilmResponseDto)
                    .toList();
        }
    }
}
