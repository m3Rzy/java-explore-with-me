package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDtoFrom;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.status.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto create(Long userId, Long eventId, CommentDtoFrom commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id " + userId + " не существует!"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с таким id " + eventId + " не существует!"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Невозможно добавить комментарий, так как события не существует!");
        }

        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreateTime(LocalDateTime.now().withNano(0));
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void delete(Long userId, Long comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Комментария с id " + comId + " не существует!"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Ошибка при удалении комментария! Возможно, пользователь не оставлял " +
                    "этот комментарий.");
        }
        commentRepository.deleteById(comId);
    }

    @Transactional
    @Override
    public CommentDto update(Long userId, Long comId, CommentDtoFrom commentDtoFrom) {
        Comment newComment = CommentMapper.toComment(commentDtoFrom);

        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Комментария с id " + comId + " не существует!"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Ошибка при редактировании комментария! Возможно, пользователь не оставлял " +
                    "этот комментарий.");
        }
        comment.setText(newComment.getText());
        comment.setCreateTime(LocalDateTime.now().withNano(0));
        return CommentMapper.toCommentDto(comment);
    }
}
