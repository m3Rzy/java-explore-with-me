package ru.practicum.event.service.admin;

import ru.practicum.event.dto.AdminEventRequestDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.status.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    EventFullDto updateAdminEvent(Long eventId, AdminEventRequestDto requestAdminDto);

    List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                      LocalDateTime start, LocalDateTime end, int from, int size);
}
