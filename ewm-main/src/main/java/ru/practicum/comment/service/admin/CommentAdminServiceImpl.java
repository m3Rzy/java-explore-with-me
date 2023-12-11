package ru.practicum.comment.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentDto> findByText(String text) {
        return CommentMapper.toListCommentDto(commentRepository.findByText(text));
    }

    @Override
    public List<CommentDto> findByUserId(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с таким id " + userId + " не существует!");
        }
        return CommentMapper.toListCommentDto(commentRepository.findAllByAuthorId(userId));
    }

    @Override
    public void delete(Long comId) {
        if (commentRepository.existsById(comId)) {
            throw new NotFoundException("Комментария с id " + comId + " не существует!");
        }
        commentRepository.deleteById(comId);
    }
}