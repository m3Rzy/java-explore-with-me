package ru.practicum.service;

import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    StatDto create(StatDto statDto);

    List<StatResponseDto> read(LocalDateTime start,
                               LocalDateTime end, List<String> uris, boolean unique);
}
