package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long bookerId);

    BookingResponseDto approvedBooking(Long bookingId, Long ownerId, Boolean approved);

    BookingResponseDto getBookingByBookerOrOwner(Long userId, Long bookingId);

    List<BookingResponseDto> getAllBookingsByBookerId(Long userId, String state);

    List<BookingResponseDto> getAllBookingsByOwnerId(Long ownerId, String state);
}
