package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponseDto {
    private Integer id; // целочисленный идентификатор
    private String login; // логин пользователя
    private String email; // электронная почта
    private String name; // имя для отображения
    private LocalDate birthday; // дата рождения
}
