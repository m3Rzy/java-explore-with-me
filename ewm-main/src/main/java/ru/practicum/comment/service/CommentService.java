package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;

import java.util.List;

public interface CommentService {

    CommentDto getById(Long comId);

    List<CommentShortDto> getByEventId(Long eventId, int from, int size);
}
