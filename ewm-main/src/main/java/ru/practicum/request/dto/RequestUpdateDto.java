package ru.practicum.request.dto;

import lombok.Data;
import ru.practicum.request.model.Request;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestUpdateDto {
    private List<Request> conformedRequest = new ArrayList<>();
    private List<Request> canselRequest = new ArrayList<>();
}
