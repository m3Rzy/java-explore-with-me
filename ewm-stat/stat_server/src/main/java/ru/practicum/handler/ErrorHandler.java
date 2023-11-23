package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.BadRequestException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(BadRequestException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(),
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now().format(FORMATTER));
    }
}

