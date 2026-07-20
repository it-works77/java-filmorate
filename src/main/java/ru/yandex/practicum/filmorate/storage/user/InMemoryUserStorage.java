package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final UniqueConstraint userStorageUniqueConstraint = new UniqueConstraint();
    private int currentId = 1; // Максимальный занятый идентификатор. Освободившиеся не занимаем

    /**
     * @param user
     * @return
     */
    @Override
    public User add(User user) {
        if (!userStorageUniqueConstraint.isInsertValid(user)) {
            throw new EntityAlreadyExistsException("Пользователь с логином %s уже существует"
                    .formatted(user.getLogin()));
        }

        int id = getNextId();
        User newUser = User.builder()
                .id(id)
                .login(user.getLogin())
                .email(user.getEmail())
                // TODO Проверить, что в сервисе UserService соблюдаем требование ТЗ:
                // имя для отображения может быть пустым — в таком случае будет использован логин
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();

        users.put(id, newUser);
        userStorageUniqueConstraint.add(newUser);
        return newUser;
    }

    /**
     * @param newUser
     * @return
     */
    @Override
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

        if (!userStorageUniqueConstraint.isUpdateValid(newUser)) {
            String warnMessage = "Пользователь с логином %s уже сохранен с другим Id=%d"
                    .formatted(newUser.getLogin(), newUser.getId());
            throw new EntityAlreadyExistsException(warnMessage);
        }

        users.put(newUser.getId(), newUser);
        log.debug("Обновили пользователя {}", newUser);
        return newUser;
    }

    /**
     * @param id пользователя
     * @return пользователя, если есть в хранилище
     */
    @Override
    public Optional<User> get(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * @return всех пользователей в хранилище
     */
    @Override
    public Collection<User> getAll() {
        return users.values().stream()
                .map(User::of)
                .collect(Collectors.toList());
    }

    /**
     * @param id пользователя
     * @return удаленного пользователя или null, если таким id не было
     */
    @Override
    public Optional<User> remove(Integer id) {
        User user = users.get(id);
        if (user == null) {
            return Optional.empty();
        } else {
            userStorageUniqueConstraint.remove(user);
            return Optional.of(users.remove(id));
        }
    }

    private Integer getNextId() {
        return currentId++;
    }

    private class UniqueConstraint {
        Map<String, Integer> constraints = new HashMap<>();

        void add(User user) {
            constraints.put(getConstraintKey(user), user.getId());
        }

        boolean isInsertValid(User user) {
            return !constraints.containsKey(getConstraintKey(user));
        }

        boolean isUpdateValid(User user) {
            Integer constraintId = constraints.get(getConstraintKey(user));
            if (constraintId == null) {
                // Ограничения нет в constraints
                return true;
            }
            // Ограничение есть, но оно сохранено для обновляемого объекта
            return constraintId.equals(user.getId());
        }

        boolean remove(User user) {
            Integer removedUserId = constraints.remove(getConstraintKey(user));
            return removedUserId != null;
        }

        private String getConstraintKey(User user) {
            return user.getLogin();
        }
    }
}
