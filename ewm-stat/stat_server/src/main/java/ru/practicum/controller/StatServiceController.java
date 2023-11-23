package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class StatServiceController {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<StatDto> addStatEvent(@RequestBody @Validated StatDto statDto) {
        StatDto statEvent = statService.create(statDto);
        return new ResponseEntity<>(statEvent, HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatResponseDto>> readStatEvent(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,

            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,

            @RequestParam(defaultValue = "") List<String> uris,

            @RequestParam(defaultValue = "false") boolean unique) {
        List<StatResponseDto> stats = statService.read(start, end, uris, unique);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
