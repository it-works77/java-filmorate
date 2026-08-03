package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /*
     * создание пользователя;
     * */
    @PostMapping
    public UserResponseDto create(@Validated(Create.class) @RequestBody UserRequestDto userRequestDto) {
        /* Добавьте логирование для операций, которые изменяют сущности — добавляют и обновляют их.
         * Также логируйте причины ошибок — например, если валидация не пройдена.
         * */
        log.info("Create user: {}", userRequestDto);
        User user = userService.add(UserMapper.mapUserRequestDtoToUser(userRequestDto));
        log.info("User created: {}", user);
        return UserMapper.mapUserToUserResponseDto(user);
    }

    /*
     * обновление пользователя;
     * */
    @PutMapping
    public UserResponseDto update(@Validated(Update.class) @RequestBody UserRequestDto userRequestDto) {
        log.info("Update user: {}", userRequestDto);
        User user = userService.update(UserMapper.mapUserRequestDtoToUser(userRequestDto));
        log.info("User updated: {}", user);
        return UserMapper.mapUserToUserResponseDto(user);
    }

    /*
     * получение пользователя по id
     */
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable @Positive Integer id) {
        log.info("Получаем пользователя по id={}", id);
        return UserMapper.mapUserToUserResponseDto(userService.get(id));
    }

    /*
     * получение списка всех пользователей.
     */
    @GetMapping
    public Collection<UserResponseDto> getAll() {
        log.info("Get all users");
        return userService.getAll().stream()
                .map(UserMapper::mapUserToUserResponseDto)
                .toList();
    }

    /*
     * добавление в друзья
     * */
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable @Positive Integer id,
                        @PathVariable @Positive Integer friendId) {
        log.info("Пользователь c id={} добавляет в друзья id={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    /*
     * возвращаем список пользователей, являющихся его друзьями
     * */
    @GetMapping("/{id}/friends")
    public Collection<UserResponseDto> getFriends(@PathVariable @Positive Integer id) {
        log.info("Получаем друзей пользователя id={}", id);
        return userService.getFriends(id).stream()
                .map(UserMapper::mapUserToUserResponseDto)
                .toList();
    }

    /*
     * удаление из друзей
     * */
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable @Positive Integer id,
                           @PathVariable @Positive Integer friendId) {
        log.info("Пользователь c id={} удаляет из друзей id={}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    /*
     * список друзей, общих с другим пользователем
     * */
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserResponseDto> getCommonFriends(@PathVariable @Positive Integer id,
                                 @PathVariable @Positive Integer otherId) {
        log.info("Общие друзья пользователей id={} и id={}", id, otherId);
        return userService.getCommonFriends(id, otherId).stream()
                .map(UserMapper::mapUserToUserResponseDto)
                .toList();
    }
}
