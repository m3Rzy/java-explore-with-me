package ru.practicum.exception;

public class UniqueException extends RuntimeException {
    public UniqueException(String message) {
        super(message);
    }
}
