package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorhandler.exception.ItemNotFoundException;
import ru.practicum.shareit.errorhandler.exception.UserNotFoundException;
import ru.practicum.shareit.errorhandler.exception.ValidateException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           @Qualifier("userRepositoryImpl") UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item createItem(Long userId, Item item) {
        validator(item);
        User user = userRepository.getUserById(userId)
                        .orElseThrow(() -> new UserNotFoundException("пользователь с id " + userId + " не найден"));

        item.setOwnerId(user.getId());

        itemRepository.createItem(item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Long userId, Item item) {
       Item existingItem = getItemById(itemId);

        if (!existingItem.getOwnerId().equals(userId)) {
            throw new ItemNotFoundException("обновлять Item может только владелец");
        }

        return itemRepository.updateItem(itemId, item);
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return itemRepository.getAllItemsByUserId(userId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.getItemById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item с id " + id + " не найден"));
    }

    @Override
    public List<Item> searchItemByText(String text) {
        return itemRepository.searchItemByText(text);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    private void validator(Item item) {
        if (item == null) {
            throw new ItemNotFoundException("Item не найден");
        }

        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidateException("имя Item должно быть заполнено");
        }

        if (item.getAvailable() == null) {
            throw new ValidateException("поле available должно быть заполнено");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidateException("поле description должно быть заполнено");
        }
    }
}
