package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.event.dto.EventCommentDto;
import ru.practicum.user.dto.UserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private UserDto author;
    private EventCommentDto event;
    private String createTime;
}
