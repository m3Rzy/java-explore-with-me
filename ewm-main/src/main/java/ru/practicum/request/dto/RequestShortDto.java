package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.request.model.status.Status;

import java.util.List;

@Data
@Builder
public class RequestShortDto {
    private List<Long> requestIds;
    private Status status;
}
