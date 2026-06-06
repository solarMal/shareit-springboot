package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.errorhandler.exception.ItemNotFoundException;
import ru.practicum.shareit.errorhandler.exception.ItemOwnerException;
import ru.practicum.shareit.errorhandler.exception.UserNotFoundException;
import ru.practicum.shareit.errorhandler.exception.ValidateException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

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

    @Override
    public CommentResponseDto createComment(CommentRequestDto requestDto,
                                            Long itemId,
                                            Long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ItemNotFoundException("Item with id " + itemId + " not found"));

        User author = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + userId + " not found"));

        List<Booking> bookings = bookingRepository.findAllByBookerId(userId);

        Booking bookingForItem = null;

        for (Booking booking : bookings) {
            if (booking.getItem().getId().equals(itemId)
                    && booking.getEnd().isBefore(LocalDateTime.now())
                    && booking.getStatus() == BookingStatus.APPROVED) {

                bookingForItem = booking;
                break;
            }
        }

        if (bookingForItem == null) {
            throw new ValidateException(
                    "Only a user who has rented this item can leave a comment");
        }

        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setText(requestDto.getText());
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toCommentResponseDto(savedComment);
    }

    @Override
    public ItemWithCommentsDto getItemById(Long itemId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + userId + " not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ItemNotFoundException("Item with id " + itemId + " not found"));

        List<CommentResponseDto> comments = commentRepository
                .findAllCommentsByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentResponseDto)
                .toList();

        BookingShortDto lastBookingDto = null;
        BookingShortDto nextBookingDto = null;

        if (item.getUser().getId().equals(user.getId())) {

            Booking lastBooking = bookingRepository
                    .findFirstByItemIdAndStartBeforeOrderByStartDesc(
                            itemId,
                            LocalDateTime.now());

            Booking nextBooking = bookingRepository
                    .findFirstByItemIdAndStartAfterOrderByStartAsc(
                            itemId,
                            LocalDateTime.now());

            lastBookingDto = lastBooking == null
                    ? null
                    : BookingMapper.toBookingShortDto(lastBooking);

            nextBookingDto = nextBooking == null
                    ? null
                    : BookingMapper.toBookingShortDto(nextBooking);
        }

        return ItemMapper.toItemWithCommentsDto(
                item,
                comments,
                lastBookingDto,
                nextBookingDto
        );
    }

}
