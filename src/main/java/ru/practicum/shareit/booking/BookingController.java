package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody BookingRequestDto dto,
                                            @RequestHeader("X-Sharer-User-Id") Long bookingId) {
        return service.createBooking(dto, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approverBooking(@PathVariable Long bookingId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam Boolean approved) {
        return service.approvedBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingByUserOrOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable Long bookingId) {
        return service.getBookingByBookerOrOwner(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllBookingsByOwnerId(ownerId, state);
    }

}
