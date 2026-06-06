package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private BookingItemDto item;
    private BookingUserDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}
