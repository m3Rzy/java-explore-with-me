package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.AdminUserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.UniqueException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public AdminUserDto createUser(User user) {
        if (userRepository.isExistEmail(user.getEmail())) {
            throw new UniqueException("Пользователь с такой почтой уже зарегистрирован.");
        }
        return UserMapper.toAdminUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя не существует.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> readUsers(List<Long> idList, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.Direction.ASC, "id");

        if (idList.isEmpty()) {
            return userRepository.findUsersWithPageable(pageable);
        }
        List<User> users = userRepository.findUserByIdWithPageable(idList, pageable);
        if (users.isEmpty()) {
            return List.of();
        }
        return users;
    }
}