package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /*
     * создание пользователя;
     * */
    @PostMapping
    public User create(@RequestBody User user) {
        // TODO
        return user;
    }

    /*
     * обновление пользователя;
     * */
    @PutMapping
    public User update(@RequestBody User newUser) {
        // TODO
        return newUser;
    }

    /*
     * получение списка всех пользователей.
     */
    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }
}
