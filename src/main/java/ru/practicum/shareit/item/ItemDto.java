package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private Boolean available;
    private String description;
}
