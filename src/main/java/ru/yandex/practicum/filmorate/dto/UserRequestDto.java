package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.time.LocalDate;


@Data
public class UserRequestDto {
    /*
     * - электронная почта не может быть пустой и должна содержать символ @;
     * - логин не может быть пустым и содержать пробелы;
     * - имя для отображения может быть пустым — в таком случае будет использован логин;
     * - дата рождения не может быть в будущем.
     */
    @Null(groups = {Create.class})
    @NotNull(groups = {Update.class})
    @Positive(groups = {Update.class})
    @EqualsAndHashCode.Exclude
    private Integer id; // целочисленный идентификатор

    @NotBlank(groups = {Create.class, Update.class}, message = "логин не может быть пустым")
    @Pattern(groups = {Create.class, Update.class},
            regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    private String login; // логин пользователя

    @NotNull(groups = {Create.class})
    @Email(groups = {Update.class},
            message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email; // электронная почта

    // имя для отображения может быть пустым — в таком случае будет использован логин;
    private String name = ""; // имя для отображения

    @PastOrPresent(groups = {Create.class, Update.class}, message = "дата рождения не может быть в будущем")
    private LocalDate birthday; // дата рождения
}
