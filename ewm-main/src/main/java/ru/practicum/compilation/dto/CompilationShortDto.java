package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.event.dto.EventDto;

import java.util.List;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationShortDto {
    private List<EventDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
