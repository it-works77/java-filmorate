package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MpaRequestDto {
    @NotNull(message = "ID MPA не может быть null")
    @Positive(message = "ID MPA должен быть положительным числом")
    private Integer id;
}
