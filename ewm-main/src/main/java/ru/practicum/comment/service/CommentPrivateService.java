package ru.practicum.comment.service;


import ru.practicum.comment.dto.CommentDtoFrom;
import ru.practicum.comment.dto.CommentDto;


public interface CommentPrivateService {

    CommentDto create(Long userId, Long eventId, CommentDtoFrom commentDto);

    CommentDto update(Long userId, Long comId, CommentDtoFrom commentDtoFrom);

    void delete(Long userId, Long comId);
}
