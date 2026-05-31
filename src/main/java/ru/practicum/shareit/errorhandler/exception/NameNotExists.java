package ru.practicum.shareit.errorhandler.exception;

public class NameNotExists extends RuntimeException {
    public NameNotExists(String message) {
        super(message);
    }
}
