package org.zeta.resturant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeta.resturant.model.TableBooking;
import org.zeta.resturant.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public TableBooking bookTable(@RequestBody TableBooking booking) {
        logger.info("Booking table for customer: {}", booking.getCustomerName());
        TableBooking booked = bookingService.bookTable(booking);
        logger.info("Table booked with id: {}", booked.getId());
        return booked;
    }

    @GetMapping
    public List<TableBooking> getAllBookings() {
        logger.info("Fetching all table bookings");
        List<TableBooking> bookings = bookingService.getAllBookings();
        logger.info("Fetched {} bookings", bookings.size());
        return bookings;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        logger.info("Cancelling booking with id: {}", id);
        bookingService.cancelBooking(id);
        logger.info("Booking cancelled with id: {}", id);
        return ResponseEntity.ok().build();
    }
}