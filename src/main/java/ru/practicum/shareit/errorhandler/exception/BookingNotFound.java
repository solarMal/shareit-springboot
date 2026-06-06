package ru.practicum.shareit.errorhandler.exception;

public class BookingNotFound extends RuntimeException {
    public BookingNotFound(String message) {
        super(message);
    }
}
