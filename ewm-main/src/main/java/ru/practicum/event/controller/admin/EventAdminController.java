package ru.practicum.event.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Validator;
import ru.practicum.event.dto.AdminEventRequestDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.admin.EventAdminService;
import ru.practicum.event.model.status.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/events")
public class EventAdminController {

    public static final String TIME_STRING = "yyyy-MM-dd HH:mm:ss";

    private final EventAdminService eventAdminService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAdminEvents(@RequestParam(required = false) List<Long> users,
                                                             @RequestParam(required = false) List<State> states,
                                                             @RequestParam(required = false) List<Long> categories,
                                                             @RequestParam(required = false)
                                                             @DateTimeFormat(pattern = TIME_STRING) LocalDateTime start,
                                                             @RequestParam(required = false)
                                                             @DateTimeFormat(pattern = TIME_STRING) LocalDateTime end,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                             @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(eventAdminService.getAdminEvents(users, states, categories, start, end, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateAdminEvent(
            @PathVariable Long eventId,
            @RequestBody @Validated({Validator.Update.class}) AdminEventRequestDto adminEventDto) {
        return ResponseEntity.ok(eventAdminService.updateAdminEvent(eventId, adminEventDto));
    }
}
