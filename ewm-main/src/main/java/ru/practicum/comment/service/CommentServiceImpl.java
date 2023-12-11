package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto getById(Long comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Комментария с id " + comId + " не существует!"));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentShortDto> getByEventId(Long eventId, int from, int size) {
        if (eventRepository.existsById(eventId)) {
                throw new NotFoundException("События с таким id " + eventId + " не существует!");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size,
                Sort.by("createTime").ascending());

        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable);
        return CommentMapper.toListCommentShortDto(comments);
    }
}
