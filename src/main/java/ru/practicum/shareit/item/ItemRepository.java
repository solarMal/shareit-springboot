package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Long itemId, Item item);

    List<Item> getAllItemsByUserId(long userId);

    Optional<Item> getItemById(Long itemId);

    List<Item> searchItemByText(String text);

    void deleteByUserIdAndItemId(long userId, long itemId);
}
