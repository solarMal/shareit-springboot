package ru.practicum.shareit.errorhandler.exception;

public class EmailNotExists extends RuntimeException {
    public EmailNotExists(String message) {
        super(message);
    }
}
