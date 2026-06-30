package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.time.LocalDate;

@Data
@Builder
public class User {
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
    @Builder.Default
    private String name = ""; // имя для отображения

    @PastOrPresent(groups = {Create.class, Update.class}, message = "дата рождения не может быть в будущем")
    private LocalDate birthday; // дата рождения

    public static User of(User user) {
        return User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}
