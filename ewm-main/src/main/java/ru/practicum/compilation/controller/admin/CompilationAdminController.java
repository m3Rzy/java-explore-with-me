package ru.practicum.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.admin.CompilationAdminService;
import ru.practicum.dto.Validator;

@RestController
@Validated
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminService service;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(
            @RequestBody @Validated(Validator.Create.class) NewCompilationDto newCompilationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.addCompilation(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<String> deleteCompilationById(@PathVariable Long compId) {
        service.deleteCompilationById(compId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Сборка успешно была удалена! " + compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable Long compId,
            @RequestBody @Validated(Validator.Update.class) NewCompilationDto newCompilationDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateCompilation(compId, newCompilationDto));
    }
}
