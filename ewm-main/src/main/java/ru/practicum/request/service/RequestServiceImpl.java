package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.model.status.Status;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.model.status.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ConfirmedRequestShortDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<RequestDto> getAllRequest(Long userId) {
        return RequestMapper.toListRequestDto(requestRepository.findAllByRequesterId(userId));
    }

    @Transactional
    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        Status status;
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События не существует!"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Владелец не может отправить заявку!");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие должно быть публичным!");
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Вы уже участвуете!");
        }

        status = (!event.getRequestModeration() ||
                event.getParticipantLimit() == 0) ? Status.CONFIRMED : Status.PENDING;


        List<ConfirmedRequestShortDto> confirmed = requestRepository.countByEventId(List.of(eventId));

        if (confirmed.size() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Свободных мест нет! Попробуйте позже.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя не существует!"));

        Request request = requestRepository.save(Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now().withNano(0))
                .status(status)
                .build());

        return RequestMapper.toRequestDto(request);
    }


    @Transactional
    @Override
    public RequestDto updateRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя не существует!");
        }
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запроса не существует!"));

        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(request);
    }
}
