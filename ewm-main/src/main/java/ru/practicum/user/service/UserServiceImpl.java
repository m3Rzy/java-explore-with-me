package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UniqueException;
import ru.practicum.user.dto.AdminUserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public AdminUserDto createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UniqueException("User with this email already exists");
        }
        return UserMapper.toAdminUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getUsers(List<Long> idList, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.Direction.ASC, "id");

        if (idList.isEmpty()) {
            return userRepository.findAllUser(pageable);
        }
        List<User> users = userRepository.findAllById(idList, pageable);
        if (users.isEmpty()) {
            return List.of();
        }
        return users;
    }
}
