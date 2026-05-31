package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorhandler.exception.ItemNotFoundException;
import ru.practicum.shareit.errorhandler.exception.ItemOwnerException;
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
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidateException("Item name not exist");
        }

        if (itemDto.getAvailable() == null) {
            throw new ValidateException("Item available not exist");
        }

        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidateException("Item description not exist");
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setUser(user);

        Item savedItem = itemRepository.save(item);

        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id" + itemId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new ItemOwnerException("User is not owner this Item");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item savedItem = itemRepository.save(item);

        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(long userId) {
        List<Item> items = itemRepository.findAllByUserId(userId);

        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(Long id) {
        return ItemMapper.toItemDto(
          itemRepository.findById(id)
                  .orElseThrow(() -> new ItemNotFoundException("Item with id " + id + " not found"))
        );
    }

    @Override
    public List<ItemDto> searchItemByText(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository.searchByText(text);

        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

}
