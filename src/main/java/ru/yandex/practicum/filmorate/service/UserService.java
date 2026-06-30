package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1; // Максимальный занятый идентификатор. Освободившиеся не занимаем

    /*
     * - электронная почта не может быть пустой и должна содержать символ @;
     * - логин не может быть пустым и содержать пробелы;
     * - имя для отображения может быть пустым — в таком случае будет использован логин;
     * - дата рождения не может быть в будущем.
     */

    public Collection<User> getAll() {
        return users.values().stream()
                .map(User::of)
                .collect(Collectors.toList());
    }

    public User add(User user) {
        Optional<User> oldUser = getUser(user);
        if (oldUser.isPresent()) {
            log.warn("Пользователь с логином {} уже существует", user.getLogin());
            throw new EntityAlreadyExistsException("Пользователь с логином %s уже существует"
                    .formatted(user.getLogin()));
        }

        // ТЗ: имя для отображения может быть пустым — в таком случае будет использован логин
        String userName = user.getName() == null ? user.getLogin() : user.getName();

        int id = getId();
        User newUser = User.builder()
                .id(id)
                .login(user.getLogin())
                .email(user.getEmail())
                .name(userName)
                .birthday(user.getBirthday())
                .build();

        users.put(id, newUser);
        return newUser;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn("При обновлении пользователя не задан его id");
            throw new IllegalArgumentException("При обновлении пользователя не задан его id");
        }

        if (!users.containsKey(newUser.getId())) {
            log.warn("Не удалось обновить пользователя. Нет такого id={}", newUser.getId());
            throw new EntityNotFoundException("Не удалось обновить пользователя. Нет такого id=%d"
                    .formatted(newUser.getId()));
        }

        // Проверим, есть ли такой объект без учета id
        // TODO Для пользователей избыточно проверять по всем полям, достаточно логина.
        Optional<User> oldUser = getUser(newUser);
        if (oldUser.isPresent()) {
            if (oldUser.get().getId()
                    .equals(newUser.getId())) {
                log.debug("Такой пользователь уже сохранен с тем же самым Id {}", newUser);
                return newUser;
            } else {
                String warnMessage = "Пользователь %s уже сохранен с другим Id=%d"
                        .formatted(newUser.getLogin(), oldUser.get().getId());
                log.warn(warnMessage);
                throw new EntityAlreadyExistsException(warnMessage);
            }
        }

        users.put(newUser.getId(), newUser);
        log.debug("Обновили пользователя {}", newUser);
        return newUser;
    }

    private Optional<User> getUser(User user) {
        return users.values().stream()
                .filter(user::equals)
                .findFirst();
    }

    private Integer getId() {
        return currentId++;
    }
}
