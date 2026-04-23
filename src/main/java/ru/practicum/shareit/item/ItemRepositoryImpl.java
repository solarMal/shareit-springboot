package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorhandler.exception.ItemNotFoundException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long dynamicId = 1;

    @Override
    public Item createItem(Item item) {
        item.setId(dynamicId++);
        items.put(item.getId(), item);
        log.info("item {} создан", item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        Item existingItem = items.get(itemId);

        if (existingItem == null) {
            throw new ItemNotFoundException("Item не найден");
        }

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }

        return existingItem;
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        List<Item> result = items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
        log.info("пользователь с id={} сохранил {} ссылок", userId, result.size());
        return result;
    }

    @Override
    public List<Item> searchItemByText(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String lowerText = text.toLowerCase();

        List<Item> itemList = items.values().stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        (item.getName() != null && item.getName().toLowerCase().contains(lowerText)) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase()
                                        .contains(lowerText))
                )
                .collect(Collectors.toList());
        log.info("найденные вещи по текстовому запросу {} ", itemList);
        return itemList;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        Item item = items.get(itemId);

        if (item != null && item.getOwnerId() == userId) {
            items.remove(itemId);
            log.info("пользователь с id={} удалил ссылку с id={}", userId, itemId);
        }
    }
}
