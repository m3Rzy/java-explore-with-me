package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.event.model.status.State;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullWithCommentShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
    private Long confirmedRequests;
    private String publishedOn;
    private List<CommentShortDto> comments;
}
