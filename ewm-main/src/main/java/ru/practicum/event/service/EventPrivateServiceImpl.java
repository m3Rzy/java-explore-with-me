package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.status.State;
import ru.practicum.event.model.status.UserEventStatus;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.event.dto.RequestShortUpdateDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.PatchEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.dto.RequestShortDto;
import ru.practicum.request.dto.RequestUpdateDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stat.service.StatService;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.request.model.status.Status;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final StatService statService;

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, EventRequestDto eventRequestDto) {
        Event event = EventMapper.toEvent(eventRequestDto);
        event.setCategory(categoryRepository.findById(event.getCategory().getId()).orElseThrow(()
                -> new NotFoundException("Категория не найдена!")));
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        event.setLocation(locationRepository.save(event.getLocation()));
        event.setInitiator(userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователя не существует!")));

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventByUserId(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя не существует!");
        }
        List<Event> events = eventRepository
                .findAllByInitiatorId(userId, PageRequest.of(from, size, Sort.Direction.ASC, "id"));

        if (events.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(events);

        Map<Long, Long> mapView = statService.toView(events);

        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(mapView.getOrDefault(event.getId(), 0L));
        }
        return EventMapper.toListEventFullDto(events);
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        Event event = eventRepository
                .findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено!"));

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(List.of(event));

        Map<Long, Long> mapView = statService.toView(List.of(event));

        event.setView(mapView.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        return EventMapper.toEventFullDto(event);
    }


    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto receivedDto) {
        PatchEventDto patchEventDto = EventMapper.toEventFromUpdateEvent(receivedDto);
        Event event = eventRepository
                .findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено!"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Вы не владелец мероприятия. Нет прав!");
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано, невозможно изменить!");
        }

        LocalDateTime eventTime = patchEventDto.getEventDate();
        if (eventTime != null) {
            if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Дата и время проведения мероприятия не могут быть ранее," +
                        " чем за 2 часа до этого момента!");
            }
            event.setEventDate(eventTime);
        }

        UserEventStatus status = patchEventDto.getStateAction();
        if (status != null) {
            if (status.equals(UserEventStatus.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
            if (status.equals(UserEventStatus.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }
        if (patchEventDto.getPaid() != null) {
            event.setPaid(patchEventDto.getPaid());
        }
        if (patchEventDto.getRequestModeration() != null) {
            event.setRequestModeration(patchEventDto.getRequestModeration());
        }


        if (patchEventDto.getAnnotation() != null && !patchEventDto.getAnnotation().isBlank()) {
            event.setAnnotation(patchEventDto.getAnnotation());
        }
        if (patchEventDto.getTitle() != null && !patchEventDto.getTitle().isBlank()) {
            event.setTitle(patchEventDto.getTitle());
        }
        if (patchEventDto.getDescription() != null && !patchEventDto.getDescription().isBlank()) {
            event.setDescription(patchEventDto.getDescription());
        }
        if (patchEventDto.getLocation() != null) {
            event.setLocation(getLocation(patchEventDto.getLocation()).orElse(saveLocation(patchEventDto.getLocation())));
        }
        if (patchEventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(patchEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не существует!")));
        }


        Map<Long, Long> view = statService.toView(List.of(event));
        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(List.of(event));

        event.setView(view.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        return EventMapper.toEventFullDto(event);
    }


    @Override
    public List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new ConflictException("Вы не владелец события. Нет прав!");
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return RequestMapper.toListRequestDto(requests);
    }

    @Transactional
    @Override
    public RequestShortUpdateDto updateRequestByOwner(Long userId, Long eventId, ru.practicum.event.dto.RequestShortDto requestShortDto) {
        RequestShortDto requestShort = RequestMapper.toRequestShort(requestShortDto);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя не существует!");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События не существует!"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Вы не владелец события. Нет прав!");
        }

        int confirmedRequest = statService.toConfirmedRequest(List.of(event)).values().size();

        if (event.getParticipantLimit() != 0 && confirmedRequest >= event.getParticipantLimit()) {
            throw new ConflictException("Свободных мест нет!");
        }

        RequestUpdateDto updateRequest = new RequestUpdateDto();

        requestShort.getRequestIds().forEach(requestId -> {

            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Запроса не существует!"));

            if (requestShort.getStatus().equals(Status.CONFIRMED)) {
                request.setStatus(Status.CONFIRMED);
                updateRequest.getConformedRequest().add(request);
            }
            if (requestShort.getStatus().equals(Status.REJECTED)) {
                request.setStatus(Status.REJECTED);
                updateRequest.getCanselRequest().add(request);
            }
        });
        return RequestMapper.toRequestShortUpdateDto(updateRequest);
    }

    private Optional<Location> getLocation(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon());
    }

    private Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}
