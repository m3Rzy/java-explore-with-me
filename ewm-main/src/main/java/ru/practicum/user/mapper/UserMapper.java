package ru.practicum.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.AdminUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoFrom;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    public User toUser(UserDtoFrom userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toUserShortDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public AdminUserDto toAdminUserDto(User newUser) {
        return AdminUserDto.builder()
                .email(newUser.getEmail())
                .id(newUser.getId())
                .name(newUser.getName())
                .build();
    }

    public List<AdminUserDto> toListAdminUserDto(List<User> users) {
        return users.stream().map(UserMapper::toAdminUserDto).collect(Collectors.toList());
    }
}