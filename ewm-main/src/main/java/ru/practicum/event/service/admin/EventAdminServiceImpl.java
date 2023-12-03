package ru.practicum.event.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.AdminEventRequestDto;
import ru.practicum.event.dto.EventAdminDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.status.AdminEventStatus;
import ru.practicum.event.model.status.State;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.location.model.Location;
import ru.practicum.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatService statService;

    @Transactional
    @Override
    public EventFullDto updateAdminEvent(Long eventId, AdminEventRequestDto adminEventRequestDto) {
        EventAdminDto eventAdminDto = EventMapper.toAdminEventFromAdminDto(adminEventRequestDto);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("События с id " + eventId + " не существует!"));

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Событие уже происходит!");
        }
        if (eventAdminDto.getEventDate() != null) {
            if (eventAdminDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Неверный формат даты!");
            } else {
                event.setEventDate(eventAdminDto.getEventDate());
            }
        }
        if (eventAdminDto.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ConflictException("Невозможно уже сменить статус!");
            }

            if (eventAdminDto.getStateAction().equals(AdminEventStatus.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now().withNano(0));
            }
            if (eventAdminDto.getStateAction().equals(AdminEventStatus.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }

        if (eventAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminDto.getRequestModeration());
        }
        if (eventAdminDto.getPaid() != null) {
            event.setPaid(eventAdminDto.getPaid());
        }
        if (eventAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminDto.getParticipantLimit());
        }
        if (eventAdminDto.getLocation() != null) {
            event.setLocation(getLocation(eventAdminDto
                    .getLocation()).orElse(saveLocation(eventAdminDto.getLocation())));
        }
        if (eventAdminDto.getAnnotation() != null && !eventAdminDto.getTitle().isBlank()) {
            event.setAnnotation(eventAdminDto.getAnnotation());
        }
        if (eventAdminDto.getDescription() != null && !eventAdminDto.getDescription().isBlank()) {
            event.setDescription(eventAdminDto.getDescription());
        }
        if (eventAdminDto.getTitle() != null && !eventAdminDto.getTitle().isBlank()) {
            event.setTitle(eventAdminDto.getTitle());
        }
        if (eventAdminDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventAdminDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена!")));
        }

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(List.of(event));
        Map<Long, Long> view = statService.toView(List.of(event));


        event.setView(view.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        return EventMapper.toEventFullDto(event);
    }


    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states,
                                             List<Long> categories, LocalDateTime start,
                                             LocalDateTime end, int from, int size) {
        List<Event> events = eventRepository.findAllByParam(users, states, categories,
                start, end, PageRequest.of(from, size, Sort.Direction.ASC, "id"));

        if (events.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(events);
        Map<Long, Long> view = statService.toView(events);

        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(view.getOrDefault(event.getId(), 0L));
        }
        return EventMapper.toListEventFullDto(events);
    }

    private Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    private Optional<Location> getLocation(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon());
    }
}