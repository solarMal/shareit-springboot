package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto);

    List<ItemDto> getAllItemsByUserId(long userId);

    ItemDto getItemById(Long id);

    List<ItemDto> searchItemByText(String text);

    CommentResponseDto createComment(CommentRequestDto requestDto, Long itemId, Long userId);

    ItemWithCommentsDto getItemById(Long itemId, Long userId);

}
