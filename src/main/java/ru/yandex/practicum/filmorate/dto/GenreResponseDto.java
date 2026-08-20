package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreResponseDto {
    private Integer id;
    private String name; // маппер подставляет displayName
}