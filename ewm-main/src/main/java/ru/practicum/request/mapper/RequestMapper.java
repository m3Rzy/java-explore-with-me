package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.event.dto.RequestShortUpdateDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestShortDto;
import ru.practicum.request.dto.RequestUpdateDto;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }

    public List<RequestDto> toListRequestDto(List<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    public RequestShortDto toRequestShort(ru.practicum.event.dto.RequestShortDto shortDto) {
        return RequestShortDto.builder()
                .requestIds(shortDto.getRequestIds())
                .status(shortDto.getStatus())
                .build();
    }

    public RequestShortUpdateDto toRequestShortUpdateDto(RequestUpdateDto requestShort) {
        return RequestShortUpdateDto.builder()
                .rejectedRequests(RequestMapper.toListRequestDto(requestShort.getCanselRequest()))
                .confirmedRequests(RequestMapper.toListRequestDto(requestShort.getConformedRequest()))
                .build();
    }
}
