package ru.practicum.shareit.request;

import lombok.Data;

import java.util.Date;

@Data
public class ItemRequest {
    private Long id;
    private Long requesterId;
    private String description;
    private Date created;
}
