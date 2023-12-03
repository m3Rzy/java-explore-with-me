package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users/")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestDto> addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.addRequest(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getAllRequest(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> updateRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.updateRequest(userId, requestId));
    }
}
