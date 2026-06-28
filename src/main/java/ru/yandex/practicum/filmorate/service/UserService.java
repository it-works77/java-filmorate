package ru.yandex.practicum.filmorate.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();

    /*
     * - электронная почта не может быть пустой и должна содержать символ @;
     * - логин не может быть пустым и содержать пробелы;
     * - имя для отображения может быть пустым — в таком случае будет использован логин;
     * - дата рождения не может быть в будущем.
     */

    public Collection<User> getAll() {
        return users.values();
    }
}
