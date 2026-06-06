package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingItemDto {
    private Long id;
    private String name;
}
