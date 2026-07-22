package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

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

    public void addFriend(Integer userId, Integer friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);
        friendStorage.addFriend(userId, friendId);
        log.info("Пользователь {} добавил друга {}", userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);
        friendStorage.removeFriend(userId, friendId);
        log.info("Пользователь {} удалил друга {}", userId, friendId);
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        Collection<Integer> commonFriendsIds = getCommonFriendIds(id, otherId);
        return commonFriendsIds.stream()
                .map(userStorage::get)
                .map(userOpt -> userOpt.orElseThrow(() ->
                        new IllegalStateException("Неконсистентное состояние friendStorage" +
                                " и userStorage: не найден пользователь по Id")))
                .toList();
    }

    public Collection<User> getFriends( Integer userId) {
        checkUserExistence(userId);
        List<Integer> friendIds = friendStorage.getFriends(userId);
        return friendIds.stream()
                .map(userStorage::get)
                .map(userOpt -> userOpt.orElseThrow(() ->
                        new IllegalStateException("Неконсистентное состояние friendStorage" +
                                " и userStorage: не найден пользователь по Id")))
                .toList();
    }

    public Collection<Integer> getCommonFriendIds(Integer firstUserId, Integer secondUserId) {
        checkUserExistence(firstUserId);
        checkUserExistence(secondUserId);
        List<Integer> firstUserFriendIds = friendStorage.getFriends(firstUserId);
        List<Integer> secondUserFriendIds = friendStorage.getFriends(secondUserId);

        List<Integer> commonFriends = new ArrayList<>(firstUserFriendIds);
        commonFriends.retainAll(secondUserFriendIds);

        log.info("Общие друзья пользователя {} и {}: {}", firstUserId, secondUserId, commonFriends);
        return commonFriends;
    }

    private void checkUserExistence(Integer userId) {
        if (userStorage.get(userId).isEmpty()) {
            String msg = "Нет пользователя с id=%d".formatted(userId);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }
    }

}
