package ru.practicum.shareit.errorhandler.exception;

public class BookingDateValidationException extends RuntimeException {
    public BookingDateValidationException(String message) {
        super(message);
    }
}
