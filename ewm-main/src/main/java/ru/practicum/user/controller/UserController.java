package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.AdminUserDto;
import ru.practicum.user.dto.UserDtoFrom;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getAllUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = "10") @Positive int size) {
        List<AdminUserDto> response = UserMapper.toListAdminUserDto(userService.readUsers(ids, from, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AdminUserDto> createUser(@RequestBody @Validated UserDtoFrom userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Пользователь с id " + userId + " был успешно удалён.");
    }
}
