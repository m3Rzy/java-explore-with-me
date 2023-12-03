package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServiceRepository extends JpaRepository<Stat, Long> {

    @Query("SELECT new ru.practicum.dto.StatResponseDto(stat.ip, stat.uri, COUNT(distinct stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY stat.ip, stat.uri " +
            "ORDER BY COUNT(distinct stat.ip) DESC")
    List<StatResponseDto> findAllByTimestampBetweenStartAndEndWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatResponseDto(stat.ip, stat.uri, COUNT(stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY stat.ip, stat.uri " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatResponseDto> findAllByTimestampBetweenStartAndEndWhereIpNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatResponseDto(stat.ip, stat.uri, COUNT(distinct stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 AND stat.uri IN ?3 " +
            "GROUP BY stat.ip, stat.uri " +
            "ORDER BY COUNT(distinct stat.ip) DESC ")
    List<StatResponseDto> findAllByTimestampBetweenStartAndEndWithUrisUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.StatResponseDto(stat.ip, stat.uri, COUNT(stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 AND stat.uri IN ?3 " +
            "GROUP BY stat.ip, stat.uri " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatResponseDto> findAllByTimestampBetweenStartAndEndWithUrisIpNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query("SELECT NEW ru.practicum.dto.StatResponseDto(s.ip, s.uri, COUNT(s.ip)) " +
            "FROM Stat AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 AND s.uri IN ?3 " +
            "GROUP BY s.ip, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<StatResponseDto> findStatsWithUrisIpNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT NEW ru.practicum.dto.StatResponseDto(s.ip, s.uri, COUNT(s.ip)) " +
            "FROM Stat AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.ip, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<StatResponseDto> findStatsWhereIpNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.dto.StatResponseDto(s.ip, s.uri, COUNT(distinct s.ip)) " +
            "FROM Stat AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 AND s.uri IN ?3 " +
            "GROUP BY s.ip, s.uri " +
            "ORDER BY COUNT(distinct s.ip) DESC ")
    List<StatResponseDto> findStatsWithUrisUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
