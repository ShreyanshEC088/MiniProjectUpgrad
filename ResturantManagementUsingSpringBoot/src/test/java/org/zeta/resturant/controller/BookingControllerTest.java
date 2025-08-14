package org.zeta.resturant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.zeta.resturant.model.TableBooking;
import org.zeta.resturant.service.BookingService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookTable() {
        TableBooking booking = new TableBooking();
        TableBooking savedBooking = new TableBooking();
        when(bookingService.bookTable(booking)).thenReturn(savedBooking);

        TableBooking result = bookingController.bookTable(booking);

        assertEquals(savedBooking, result);
        verify(bookingService).bookTable(booking);
    }

    @Test
    void testGetAllBookings() {
        List<TableBooking> bookings = Arrays.asList(new TableBooking(), new TableBooking());
        when(bookingService.getAllBookings()).thenReturn(bookings);

        List<TableBooking> result = bookingController.getAllBookings();

        assertEquals(bookings, result);
        verify(bookingService).getAllBookings();
    }

    @Test
    void testCancelBooking() {
        Long id = 1L;

        ResponseEntity<?> response = bookingController.cancelBooking(id);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(bookingService).cancelBooking(id);
    }
}
