package ru.practicum.request.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedRequestShortDto {
    private Long eventId;
    private Long confirmedRequestsCount;
}
