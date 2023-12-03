package ru.practicum.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatResponseDto {
    private String app;
    private String uri;
    private long hits;
}
