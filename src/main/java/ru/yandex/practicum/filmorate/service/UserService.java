package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    /*
     * - электронная почта не может быть пустой и должна содержать символ @;
     * - логин не может быть пустым и содержать пробелы;
     * - имя для отображения может быть пустым — в таком случае будет использован логин;
     * - дата рождения не может быть в будущем.
     */

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User add(User user) {
        // ТЗ: имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        return userStorage.add(user);
    }

    public User update(User newUser) {

        User updatedUser = userStorage.update(newUser);
        log.info("Обновили пользователя {}", updatedUser);
        return updatedUser;
    }
}
