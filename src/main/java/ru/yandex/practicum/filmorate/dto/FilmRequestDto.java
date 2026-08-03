package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.NotBefore;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class FilmRequestDto {
    @Null(groups = {Create.class})
    @NotNull(groups = {Update.class})
    @Positive(groups = {Update.class})
    private Integer id; // целочисленный идентификатор

    @NotBlank(groups = {Create.class, Update.class}, message = "Название фильма не может быть пустым")
    private String name; // название

    @Size(groups = {Create.class, Update.class}, max = 200, message = "Максимальная длина описания — 200 символов")
    @NotNull(groups = {Create.class, Update.class})
    private String description; // описание

    @NotBefore(groups = {Create.class, Update.class},
            value = "1895-12-28", message = "Дата релиза не может быть раньше 28.12.1895")
    @NotNull(groups = {Create.class, Update.class})
    private LocalDate releaseDate; // дата релиза

    @Positive(groups = {Create.class, Update.class},
            message = "Продолжительность фильма должна быть положительным числом")
    private int duration; // продолжительность фильма в минутах

    private List<GenreDto> genres = new ArrayList<>(); // жанры фильма

    @NotNull(groups = {Create.class, Update.class})
    private MpaRequestDto mpa; // возрастной рейтинг
}
