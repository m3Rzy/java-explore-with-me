package ru.practicum.stat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ConfirmedRequestShortDto;
import ru.practicum.request.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StatServiceImpl implements StatService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final ObjectMapper objectMapper;

    @Value("${main_app}")
    private String app;

    @Override
    public Map<Long, Long> toConfirmedRequest(Collection<Event> list) {
        List<Long> listEventId = list.stream().map(Event::getId).collect(Collectors.toList());
        List<ConfirmedRequestShortDto> confirmedRequestShortDtoList = requestRepository.countByEventId(listEventId);
        return confirmedRequestShortDtoList.stream()
                .collect(Collectors.toMap(ConfirmedRequestShortDto::getEventId,
                        ConfirmedRequestShortDto::getConfirmedRequestsCount));
    }

    @Override
    public Map<Long, Long> toView(Collection<Event> events) {
        Map<Long, Long> view = new HashMap<>();
        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(LocalDateTime::compareTo).orElse(null);
        if (start == null) {
            return Map.of();
        }
        List<String> uris = events.stream().map(a -> "/events/" + a.getId()).collect(Collectors.toList());

        ResponseEntity<Object> response = statClient.readStatEvent(start.format(FORMATTER),
                LocalDateTime.now().format(FORMATTER), uris, true);

        try {
            StatResponseDto[] stats = objectMapper.readValue(
                    objectMapper.writeValueAsString(response.getBody()), StatResponseDto[].class);



            for (StatResponseDto stat : stats) {
                view.put(
                        Long.parseLong(stat.getUri().replaceAll("\\D+", "")),
                        stat.getHits());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка со статистикой: " + e.getMessage());
        }
        return view;
    }

    @Transactional
    @Override
    public void addHits(HttpServletRequest request) {
        statClient.addStatEvent(StatDto.builder()
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .app(app)
                .build());
    }
}
