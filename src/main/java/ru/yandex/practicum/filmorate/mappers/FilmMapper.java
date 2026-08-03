package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.enums.MpaRating;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapFilmRequestDtoToFilm(FilmRequestDto filmRequestDto) {
        MpaRating mpa = MpaRating.byCode(filmRequestDto.getMpa().getId());

        Set<Genre> genres = filmRequestDto.getGenres().stream()
                    .map( e -> Genre.byCode(e.getId()))
                    .collect(Collectors.toSet());

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
                .mpa(new MpaResponseDto(film.getMpa().getCode(),
                        film.getMpa().getDisplayName()))
                .genres(film.getGenres().stream()
                        .map(e -> new GenreResponseDto(e.getCode(), e.getDisplayName()))
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public static MpaRating MpaRequestDtoToMpaRating(MpaRequestDto mpaRequestDto) {
        return MpaRating.byCode(mpaRequestDto.getId());
    }

    public static MpaResponseDto mpaRatingToMpaResponseDto(MpaRating mpaRating) {
        return new MpaResponseDto(mpaRating.getCode(), mpaRating.getDisplayName());
    }


    public static Genre genreRequestDtoToGenre(GenreRequestDto genreRequestDto) {
        return Genre.byCode(genreRequestDto.getId());
    }

    public static GenreResponseDto genreToGenreResponseDto(Genre genre) {
        return new GenreResponseDto(genre.getCode(), genre.getDisplayName());
    }
}
