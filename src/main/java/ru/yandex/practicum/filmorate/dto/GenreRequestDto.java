package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreRequestDto {
    @NotNull(message = "ID жанра не может быть null")
    @Positive(message = "ID жанра должен быть положительным числом")
    private Integer id;
}