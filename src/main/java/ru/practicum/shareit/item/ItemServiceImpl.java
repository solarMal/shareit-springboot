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
                           UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item createItem(Long userId, Item item) {
        return new Item();
    }

    @Override
    public Item updateItem(Long itemId, Long userId, Item item) {
       return new Item();
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

}
