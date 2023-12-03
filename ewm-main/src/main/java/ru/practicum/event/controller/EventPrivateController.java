package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Validator;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventPrivateService;
import ru.practicum.request.dto.RequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventFullDto>> getEventByUserId(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(eventPrivateService.getEventByUserId(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getRequestByUserIdAndEventId(@PathVariable Long userId,
                                                                         @PathVariable Long eventId) {
        return ResponseEntity.ok(eventPrivateService.getRequestByUserIdAndEventId(userId, eventId));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUserIdAndEventId(@PathVariable Long userId,
                                                                   @PathVariable Long eventId) {
        return ResponseEntity.ok(eventPrivateService.getEventByUserIdAndEventId(userId, eventId));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId,
                                                 @RequestBody @Validated EventRequestDto eventRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventPrivateService.createEvent(userId, eventRequestDto));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                    @RequestBody @Validated(Validator.Update.class) UpdateEventDto receivedDto) {
        return ResponseEntity.ok(eventPrivateService.updateEvent(userId, eventId, receivedDto));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestShortUpdateDto> updateRequestByOwnerUser(@PathVariable Long userId,
                                                                         @PathVariable Long eventId,
                                                                         @RequestBody RequestShortDto requestShortDto) {
        return ResponseEntity.ok(eventPrivateService.updateRequestByOwner(userId, eventId, requestShortDto));
    }
}
