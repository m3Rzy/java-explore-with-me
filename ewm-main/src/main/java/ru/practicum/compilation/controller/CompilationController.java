package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService service;

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long compId) {
        CompilationDto response = service.getCompilationById(compId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilation(@RequestParam(required = false) Boolean pinned,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        List<CompilationDto> list = service.getCompilations(pinned, from, size);
        return ResponseEntity.ok(list);
    }
}
