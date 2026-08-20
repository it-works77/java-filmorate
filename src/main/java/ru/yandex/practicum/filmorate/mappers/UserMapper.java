package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapUserRequestDtoToUser(UserRequestDto userRequestDto) {
        return User.builder()
                .id(userRequestDto.getId())
                .login(userRequestDto.getLogin())
                .email(userRequestDto.getEmail())
                .name(userRequestDto.getName())
                .birthday(userRequestDto.getBirthday())
                .build();

    }

    public static UserResponseDto mapUserToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}
