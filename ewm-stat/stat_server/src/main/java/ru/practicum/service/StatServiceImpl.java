package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatServiceRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mapper.StatMapper.toStat;
import static ru.practicum.mapper.StatMapper.toStatDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StatServiceImpl implements StatService {

    private final StatServiceRepository statServiceRepository;

    @Override
    @Transactional
    public List<StatResponseDto> read(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Дата начала не может быть позже даты окончания.");
        }
        if (uris.isEmpty()) {
            return statServiceRepository.findStatsWhereIpNotUnique(start, end);
        }
        if (unique) {
            return statServiceRepository.findStatsWithUrisUniqueIp(start, end, uris);
        } else {
            return statServiceRepository.findStatsWithUrisIpNotUnique(start, end, uris);
        }
    }

    @Transactional
    public StatDto create(StatDto statDto) {
        Stat stat = statServiceRepository.save(toStat(statDto));
        log.info("Статистика успешно сохранена: {}", stat);
        return toStatDto(stat);
    }
}

