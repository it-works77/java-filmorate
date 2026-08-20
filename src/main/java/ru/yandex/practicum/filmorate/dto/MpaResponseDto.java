package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MpaResponseDto {
    private Integer id;
    private String name; // маппер подставляет displayName
}
