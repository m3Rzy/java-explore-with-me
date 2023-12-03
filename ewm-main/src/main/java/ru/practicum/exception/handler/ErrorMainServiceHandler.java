package ru.practicum.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UniqueException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorMainServiceHandler {

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class,
            BadRequestException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(final Exception e) {
        log.error("{} - Статус: {}, Описание: {}",
                "Bad Request", HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalServerError(final Exception e) {
        log.error("{} - Статус: {}, Описание: {}",
                "SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class, UniqueException.class})
    public ResponseEntity<ErrorResponse> handleConflict(final Exception e) {
        log.error("{} - Статус: {}, Описание: {}",
                "CONFLICT", HttpStatus.CONFLICT, e.getMessage());

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.CONFLICT.name(), e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(final RuntimeException e) {
        log.error("{} - Статус: {}, Описание: {}",
                "NOT FOUND", HttpStatus.NOT_FOUND, e.getMessage());

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.name(),
                e.getMessage()), HttpStatus.NOT_FOUND);
    }
}

