package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage,
                       @Qualifier("dbFriendStorage") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public UserResponseDto add(UserRequestDto userRequestDto) {
        User user = UserMapper.mapUserRequestDtoToUser(userRequestDto);
        // ТЗ: имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return UserMapper.mapUserToUserResponseDto(userStorage.add(user));
    }

    public UserResponseDto get(Integer userId) {
        User user = userStorage.get(userId).orElseThrow(() ->
                new EntityNotFoundException("Не найден пользователь с id=" + userId));
        return UserMapper.mapUserToUserResponseDto(user);
    }

    public Collection<UserResponseDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapUserToUserResponseDto)
                .toList();
    }

    public UserResponseDto update(UserRequestDto userRequestDto) {
        User newUser = UserMapper.mapUserRequestDtoToUser(userRequestDto);
        checkUserExistence(newUser.getId());
        User updatedUser = userStorage.update(newUser);
        log.info("Обновили пользователя {}", updatedUser);
        return UserMapper.mapUserToUserResponseDto(updatedUser);
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

    public Collection<UserResponseDto> getCommonFriends(Integer id, Integer otherId) {
        Collection<Integer> commonFriendsIds = getCommonFriendIds(id, otherId);
        return commonFriendsIds.stream()
                .map(userStorage::get)
                .map(userOpt -> userOpt.orElseThrow(() ->
                        new IllegalStateException("Неконсистентное состояние friendStorage" +
                                " и userStorage: не найден пользователь по Id")))
                .map(UserMapper::mapUserToUserResponseDto)
                .toList();
    }

    public Collection<UserResponseDto> getFriends(Integer userId) {
        checkUserExistence(userId);
        List<Integer> friendIds = friendStorage.getFriends(userId);
        List<User> friends = friendIds.stream()
                .map(userStorage::get)
                .map(userOpt -> userOpt.orElseThrow(() ->
                        new IllegalStateException("Неконсистентное состояние friendStorage" +
                                " и userStorage: не найден пользователь по Id")))
                .toList();
        return friends.stream()
                .map(UserMapper::mapUserToUserResponseDto)
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
