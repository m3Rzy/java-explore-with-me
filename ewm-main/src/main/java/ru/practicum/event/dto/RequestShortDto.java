package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.request.model.status.Status;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestShortDto {
    private List<Long> requestIds;
    private Status status;
}
