package ru.practicum.event.service;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.event.dto.RequestShortDto;
import ru.practicum.event.dto.RequestShortUpdateDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.UpdateEventDto;

import java.util.List;

public interface EventPrivateService {

    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId,  UpdateEventDto receivedDto);

    EventFullDto createEvent(Long userId, EventRequestDto eventRequestDto);

    List<EventFullDto> getEventByUserId(Long userId, int from, int size);

    List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId);

    RequestShortUpdateDto updateRequestByOwner(Long userId, Long eventId, RequestShortDto requestShortDto);
}
