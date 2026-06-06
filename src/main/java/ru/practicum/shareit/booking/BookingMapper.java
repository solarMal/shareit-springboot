package ru.practicum.shareit.booking;

public class BookingMapper {

    public static Booking toBookingFromRequestDto(BookingRequestDto dto) {
        Booking booking = new Booking();

        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        BookingItemDto bookingItemDto = new BookingItemDto(booking.getItem().getId(), booking.getItem().getName());
        BookingUserDto bookingUserDto = new BookingUserDto(booking.getBooker().getId());

        dto.setId(booking.getId());

        dto.setItem(bookingItemDto);
        dto.setBooker(bookingUserDto);

        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

}
