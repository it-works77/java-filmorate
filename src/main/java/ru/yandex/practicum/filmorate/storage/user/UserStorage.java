package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User newUser);

    Optional<User> get(Integer id);

    Collection<User> getAll();

    Optional<User> remove(Integer id);
}
