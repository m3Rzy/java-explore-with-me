package ru.practicum.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatServiceRepository statServiceRepository;

    @Transactional
    public StatDto createStat(StatDto statDto) {
        Stat stat = statServiceRepository.save(toStat(statDto));
        return toStatDto(stat);
    }

    @Override
    @Transactional
    public List<StatResponseDto> readStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Дата начала не может быть позже даты окончания!");
        }

        if (uris.isEmpty()) {
            if (unique) {
                return statServiceRepository.findAllByTimestampBetweenStartAndEndWithUniqueIp(start, end);
            } else {
                return statServiceRepository.findAllByTimestampBetweenStartAndEndWhereIpNotUnique(start, end);
            }
        } else {
            if (unique) {
                return statServiceRepository.findAllByTimestampBetweenStartAndEndWithUrisUniqueIp(start, end, uris);
            } else {
                return statServiceRepository.findAllByTimestampBetweenStartAndEndWithUrisIpNotUnique(start, end, uris);
            }
        }
    }
}

