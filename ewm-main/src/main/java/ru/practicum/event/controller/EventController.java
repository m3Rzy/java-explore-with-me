package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/events")
public class EventController {

    public static final String TIME_STRING = "yyyy-MM-dd HH:mm:ss";

    private final EventService service;

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getPublicEvent(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(service.getPublicEvent(id, request));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getPublicEvents(@RequestParam(required = false) String text,
                                                              @RequestParam(required = false) List<@Positive Long> categories,
                                                              @RequestParam(required = false) Boolean paid,
                                                              @RequestParam(required = false)
                                                                @DateTimeFormat(pattern = TIME_STRING) LocalDateTime rangeStart,
                                                              @RequestParam(required = false)
                                                                @DateTimeFormat(pattern = TIME_STRING) LocalDateTime rangeEnd,
                                                              @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                              @RequestParam(required = false) String sort,
                                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                              @RequestParam(defaultValue = "10") @Positive int size,
                                                              HttpServletRequest request) {
        return ResponseEntity.ok(service.getPublicEvents(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, request));
    }
}
