package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item updateItem(Long itemId, Long userId, Item item);

    List<Item> getAllItemsByUserId(long userId);

    Item getItemById(Long id);

    List<Item> searchItemByText(String text);

    void deleteItem(long userId, long itemId);
}
