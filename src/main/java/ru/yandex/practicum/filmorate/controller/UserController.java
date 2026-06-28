package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
    public User create(@RequestBody User user) {
        // TODO
        /* Добавьте логирование для операций, которые изменяют сущности — добавляют и обновляют их.
        * Также логируйте причины ошибок — например, если валидация не пройдена.
        * */
        log.info("Create user {}", user);
        return user;
    }

    /*
     * обновление пользователя;
     * */
    @PutMapping
    public User update(@RequestBody User newUser) {
        // TODO
        /* Добавьте логирование для операций, которые изменяют сущности — добавляют и обновляют их.
         * Также логируйте причины ошибок — например, если валидация не пройдена.
         * */
        log.info("Update user {}", newUser);

        return newUser;
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
