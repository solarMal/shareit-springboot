package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String name;
    private Boolean available;
    private String description;
    private Long ownerId;
    private String url;
//    private Long requestId; //вещь добавлена в ответ на запрос
}
