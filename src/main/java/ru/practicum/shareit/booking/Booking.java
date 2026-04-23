package ru.practicum.shareit.booking;

import lombok.Data;

@Data
public class Booking {
    private Long id;
    private Long itemId;
    private Long bookerId;
    private Data start;
    private Data end;
    private BookingStatus status;
}
