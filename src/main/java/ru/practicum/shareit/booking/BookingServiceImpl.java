package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorhandler.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long bookerId) {
        if (bookingRequestDto.getStart() == null || bookingRequestDto.getEnd() == null) {
            throw new BookingDateValidationException("Booking date must be exists");
        }

        if (!bookingRequestDto.getStart().isBefore(bookingRequestDto.getEnd())) {
            throw new BookingDateValidationException("Booking start date must be before end date");
        }

        if (bookingRequestDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDateValidationException("Booking start date cannot be in the past");
        }


        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException
                        ("Item with id " + bookingRequestDto.getItemId() + " not found "));

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException
                        ("Booker with id " + bookerId + " not found"));


        if (item.getUser().getId().equals(booker.getId())) {
            throw new ValidateException("Item owner cant booking this");
        }

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Item not available at this moment");
        }

        Booking booking = BookingMapper.toBookingFromRequestDto(bookingRequestDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto approvedBooking(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFound("Booking with id " + bookingId + " not found"));

        if (!booking.getItem().getUser().getId().equals(ownerId)) {
            throw new ValidateException("User is not owner of item");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidateException("Booking is not in WAITING status");
        }

        if (approved == true) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto getBookingByBookerOrOwner(Long userId, Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFound("Booking with id " + bookingId + " not found"));

        if (booking.getBooker().getId().equals(userId)
                || booking.getItem().getUser().getId().equals(userId)) {
            return BookingMapper.toBookingResponseDto(booking);
        } else {
            throw new ValidateException("User must be booker or owner");
        }
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state) {

        List<Booking> bookings = switch (state) {
            case "WAITING" ->
                    bookingRepository.findAllByBookerIdAndStatus(
                            userId,
                            BookingStatus.WAITING);

            case "REJECTED" ->
                    bookingRepository.findAllByBookerIdAndStatus(
                            userId,
                            BookingStatus.REJECTED);

            case "CURRENT" ->
                    bookingRepository.findCurrentBookerByBookerId(
                            userId,
                            LocalDateTime.now());

            case "PAST" ->
                    bookingRepository.findPastBookerByBookerId(
                            userId,
                            LocalDateTime.now());

            case "FUTURE" ->
                    bookingRepository.findFutureBookerByBookerId(
                            userId,
                            LocalDateTime.now());

            case "ALL" ->
                    bookingRepository.findAllByBookerId(userId);

            default ->
                    throw new ValidateException("Unknown state: " + state);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        List<Booking> ownerBookings = bookingRepository.findAllByItemOwnerId(ownerId);

        if (ownerBookings.isEmpty()) {
            throw new BookingNotFound("Booking for ownerId " + ownerId + " not found");
        }

        List<Booking> currentBooking = switch (state) {
            case "WAITING" ->
                    bookingRepository.findAllByOwnerIdAndStatus(
                            ownerId,
                            BookingStatus.WAITING);

            case "REJECTED" ->
                    bookingRepository.findAllByOwnerIdAndStatus(
                            ownerId,
                            BookingStatus.REJECTED);

            case "CURRENT" ->
                    bookingRepository.findCurrentBookingsByOwnerId(
                            ownerId,
                            LocalDateTime.now());

            case "PAST" ->
                    bookingRepository.findPastBookingsByOwnerId(
                            ownerId,
                            LocalDateTime.now());

            case "FUTURE" ->
                    bookingRepository.findFutureBookingsByOwnerId(
                            ownerId,
                            LocalDateTime.now());

            case "ALL" ->
                    bookingRepository.findAllByItemOwnerId(ownerId);

            default ->
                    throw new ValidateException("Unknown state: " + state);
        };

        return currentBooking.stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }
}
