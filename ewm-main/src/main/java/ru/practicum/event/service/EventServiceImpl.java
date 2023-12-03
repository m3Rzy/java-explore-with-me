package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.status.State;
import ru.practicum.exception.NotFoundException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.stat.service.StatService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final StatService statService;

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                               Integer size, HttpServletRequest request) {
        sort = (sort != null && sort.equals("EVENT_DATE")) ? "eventDate" : "id";

        List<Event> list = eventRepository.findAllEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort,
                PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(sort).descending()));

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(list);
        Map<Long, Long> view = statService.toView(list);

        List<EventDto> events = new ArrayList<>();

        list.forEach(event -> events.add(EventMapper.toEventShort(event, view.getOrDefault(event.getId(), 0L),
                confirmedRequest.getOrDefault(event.getId(), 0L))));

        statService.addHits(request);
        return EventMapper.toListEventShortDto(events);
    }

    @Override
    public EventFullDto getPublicEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("События не существует!"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Событие должно быть уже опубликовано!");
        }

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(List.of(event));
        Map<Long, Long> view = statService.toView(List.of(event));
        statService.addHits(request);
        event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
        event.setView(view.getOrDefault(event.getId(), 0L));
        return EventMapper.toEventFullDto(event);
    }
}
