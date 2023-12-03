package ru.practicum.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.dto.StatDto;
import ru.practicum.model.Stat;

@NoArgsConstructor
public class StatMapper {

    public static Stat toStat(StatDto statDto) {

        return Stat.builder()
                .ip(statDto.getIp())
                .uri(statDto.getUri())
                .timestamp(statDto.getTimestamp())
                .app(statDto.getApp())
                .build();
    }

    public static StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .timestamp(stat.getTimestamp())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .build();
    }
}
