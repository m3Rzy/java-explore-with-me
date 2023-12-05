package ru.practicum.comment.service.admin;


import ru.practicum.comment.dto.CommentDto;

import java.util.List;

public interface CommentAdminService {

    List<CommentDto> findByText(String text);

    List<CommentDto> findByUserId(Long userId);

    void delete(Long comId);
}