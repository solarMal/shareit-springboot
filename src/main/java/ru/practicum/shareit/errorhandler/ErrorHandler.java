package ru.practicum.shareit.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.errorhandler.exception.*;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFound(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ValidateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(ValidateException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CriticalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(CriticalException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse itemNotFound(ItemNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EmailNotExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse emailNotExists(EmailNotExists e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExist.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailAlreadyExists(EmailAlreadyExist e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ItemOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse itemOwnerException(ItemOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }
}
