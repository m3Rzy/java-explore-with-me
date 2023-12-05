package ru.practicum.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.dto.CommentDtoFrom;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.user.mapper.UserMapper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Comment toComment(CommentDtoFrom commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .event(EventMapper.toEventComment(comment.getEvent()))
                .createTime(comment.getCreateTime().format(FORMATTER))
                .text(comment.getText())
                .build();
    }

    public CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .createTime(comment.getText())
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }

    public List<CommentShortDto> toListCommentShortDto(List<Comment> list) {
        return list.stream().map(CommentMapper::toCommentShortDto).collect(Collectors.toList());
    }
}
