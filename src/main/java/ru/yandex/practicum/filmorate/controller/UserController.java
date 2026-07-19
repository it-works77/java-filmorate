package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    /*
     * создание пользователя;
     * */
    @PostMapping
    public User create(@Validated(Create.class) @RequestBody User user) {
        // TODO
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
}
