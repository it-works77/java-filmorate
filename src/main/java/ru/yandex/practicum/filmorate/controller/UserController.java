package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /*
     * создание пользователя;
     * */
    @PostMapping
    public User create(@Validated(Create.class) @RequestBody User user) {
        /* Добавьте логирование для операций, которые изменяют сущности — добавляют и обновляют их.
         * Также логируйте причины ошибок — например, если валидация не пройдена.
         * */
        log.info("Create user: {}", user);
        User result = userService.add(user);
        log.info("User created: {}", result);
        return result;
    }

    /*
     * обновление пользователя;
     * */
    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User newUser) {
        log.info("Update user: {}", newUser);
        User result = userService.update(newUser);
        log.info("User updated: {}", newUser);
        return result;
    }

    /*
     * получение списка всех пользователей.
     */
    @GetMapping
    public Collection<User> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    /*
     * добавление в друзья
     * */
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable @Positive Integer id,
                        @PathVariable @Positive Integer friendId) {
        log.info("Пользователь c id={} добавляет в друзья id={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    /*
     * удаление из друзей
     * */
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable @Positive Integer id,
                           @PathVariable @Positive Integer friendId) {
        log.info("Пользователь c id={} удаляет из друзей id={}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    /*
     * получение пользователя по id
     */
    @GetMapping("/{id}")
    public User getFilm(@PathVariable @Positive Integer id) {
        log.info("Получаем пользователя по id={}", id);
        return userService.get(id).orElseThrow(() ->
                new EntityNotFoundException("Не найден пользователь с id=" + id));
    }

    /*
     * список друзей, общих с другим пользователем
     * */
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable @Positive Integer id,
                                 @PathVariable @Positive Integer otherId) {
        log.info("Общие друзья пользователей id={} и id={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
