package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

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

    public Optional<User> get(Integer userId) {
        return userStorage.get(userId);
    }

    public User update(User newUser) {

        User updatedUser = userStorage.update(newUser);
        log.info("Обновили пользователя {}", updatedUser);
        return updatedUser;
    }
}
