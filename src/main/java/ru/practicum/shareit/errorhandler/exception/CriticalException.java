package ru.practicum.shareit.errorhandler.exception;

public class CriticalException extends RuntimeException {
    public CriticalException(String message) {
        super(message);
    }
}
