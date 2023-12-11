package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.status.State;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullWithComment {
    private Long id;
    private String annotation;
    private Category category;
    private String createdOn;
    private String description;
    private String eventDate;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
    private Long confirmedRequests;
    private String publishedOn;
    private List<Comment> comments;
}
