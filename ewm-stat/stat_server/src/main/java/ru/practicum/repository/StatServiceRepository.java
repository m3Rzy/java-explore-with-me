package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServiceRepository extends JpaRepository<Stat, Long> {

    @Query("select new ru.practicum.dto.StatResponseDto(s.ip, s.uri, count(s.ip)) " +
            "from Stat as s " +
            "where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.ip, s.uri " +
            "order by count(s.ip) desc ")
    List<StatResponseDto> findStatsWithUrisIpNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatResponseDto(s.ip, s.uri, count(s.ip)) " +
            "from Stat as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.ip, s.uri " +
            "order by count(s.ip) desc ")
    List<StatResponseDto> findStatsWhereIpNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatResponseDto(s.ip, s.uri, count(distinct s.ip)) " +
            "from Stat as s " +
            "where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.ip, s.uri " +
            "order by count(distinct s.ip) desc ")
    List<StatResponseDto> findStatsWithUrisUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
