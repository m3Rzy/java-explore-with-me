package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Validator;
import ru.practicum.user.dto.AdminUserDto;
import ru.practicum.user.dto.UserDtoReceived;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AdminUserDto> addUser(@RequestBody @Validated({Validator.Create.class}) UserDtoReceived userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Пользователь с id " + userId + " успешно удалён!");
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = "10") @Positive int size) {
        List<AdminUserDto> response = UserMapper.toListAdminUserDto(userService.getUsers(ids, from, size));
        return ResponseEntity.ok(response);
    }
}
