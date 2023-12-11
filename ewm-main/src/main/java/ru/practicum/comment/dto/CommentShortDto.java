package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.user.dto.UserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {
    private Long id;
    private String text;
    private UserDto author;
    private String createTime;
}
