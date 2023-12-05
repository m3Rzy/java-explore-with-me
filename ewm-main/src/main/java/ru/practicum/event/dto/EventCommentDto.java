package ru.practicum.event.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCommentDto {
    private Long id;
    private String title;
}
