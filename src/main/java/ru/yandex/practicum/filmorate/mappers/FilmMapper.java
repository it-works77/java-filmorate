package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.enums.MpaRating;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
/*
    public static Film mapFilmCreateRequestDtoToFilm(FilmRequestDto filmRequestDto) {
        return Film.builder()
                .name(filmRequestDto.getName())
                .description(filmRequestDto.getDescription())
                .releaseDate(filmRequestDto.getReleaseDate())
                .duration(filmRequestDto.getDuration())
                .mpa(MpaRating.byCode(filmRequestDto.getMpa().getId()))
                .genres(filmRequestDto.getGenres().stream()
                        .map( e -> Genre.byCode(e.getId()))
                        .toList())
                .build();
    }
*/

    public static Film mapFilmRequestDtoToFilm(FilmRequestDto filmRequestDto) {
        MpaRating mpa;
        try {
            mpa = MpaRating.byCode(filmRequestDto.getMpa().getId());
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(e.getMessage());
        }

        List<Genre> genres;
        try {
            genres = filmRequestDto.getGenres().stream()
                    .map( e -> Genre.byCode(e.getId()))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(e.getMessage());
        }

        return Film.builder()
                .id(filmRequestDto.getId())
                .name(filmRequestDto.getName())
                .description(filmRequestDto.getDescription())
                .releaseDate(filmRequestDto.getReleaseDate())
                .duration(filmRequestDto.getDuration())
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    public static FilmResponseDto mapFilmToFilmResponseDto(Film film) {
        return FilmResponseDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(new MpaDto(film.getMpa().getCode()))
                .genres(film.getGenres().stream()
                        .map(e -> new GenreDto(e.getCode()))
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
