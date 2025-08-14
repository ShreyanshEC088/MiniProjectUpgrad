package org.zeta.resturant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.zeta.resturant.dao.OrderRepository;
import org.zeta.resturant.dao.TableBookingRepository;
import org.zeta.resturant.exceptions.BookingNotFoundException;
import org.zeta.resturant.exceptions.TableOccupiedException;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.TableBooking;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private TableBookingRepository bookingRepo;
    @Mock
    private OrderRepository orderRepo;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isTableAvailable_true() {
        when(orderRepo.findByTableNumberAndStatusNot(1, Order.OrderStatus.PAID)).thenReturn(Collections.emptyList());
        when(bookingRepo.findByTableNumberAndStatus(1, TableBooking.BookingStatus.BOOKED)).thenReturn(Collections.emptyList());
        assertTrue(bookingService.isTableAvailable(1));
    }

    @Test
    void isTableAvailable_false_dueToActiveOrders() {
        when(orderRepo.findByTableNumberAndStatusNot(1, Order.OrderStatus.PAID)).thenReturn(List.of(new Order()));
        when(bookingRepo.findByTableNumberAndStatus(1, TableBooking.BookingStatus.BOOKED)).thenReturn(Collections.emptyList());
        assertFalse(bookingService.isTableAvailable(1));
    }

    @Test
    void isTableAvailable_false_dueToActiveBookings() {
        when(orderRepo.findByTableNumberAndStatusNot(1, Order.OrderStatus.PAID)).thenReturn(Collections.emptyList());
        when(bookingRepo.findByTableNumberAndStatus(1, TableBooking.BookingStatus.BOOKED)).thenReturn(List.of(new TableBooking()));
        assertFalse(bookingService.isTableAvailable(1));
    }

    @Test
    void bookTable_success() {
        TableBooking booking = new TableBooking();
        booking.setTableNumber(2);
        when(orderRepo.findByTableNumberAndStatusNot(2, Order.OrderStatus.PAID)).thenReturn(Collections.emptyList());
        when(bookingRepo.findByTableNumberAndStatus(2, TableBooking.BookingStatus.BOOKED)).thenReturn(Collections.emptyList());
        when(bookingRepo.save(any(TableBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TableBooking result = bookingService.bookTable(booking);

        assertEquals(TableBooking.BookingStatus.BOOKED, result.getStatus());
        assertNotNull(result.getBookingTime());
        verify(bookingRepo).save(booking);
    }

    @Test
    void bookTable_throwsTableOccupiedException() {
        TableBooking booking = new TableBooking();
        booking.setTableNumber(3);
        when(orderRepo.findByTableNumberAndStatusNot(3, Order.OrderStatus.PAID)).thenReturn(List.of(new Order()));
        when(bookingRepo.findByTableNumberAndStatus(3, TableBooking.BookingStatus.BOOKED)).thenReturn(Collections.emptyList());

        assertThrows(TableOccupiedException.class, () -> bookingService.bookTable(booking));
    }

    @Test
    void getAllBookings_returnsList() {
        List<TableBooking> bookings = List.of(new TableBooking(), new TableBooking());
        when(bookingRepo.findAll()).thenReturn(bookings);

        List<TableBooking> result = bookingService.getAllBookings();

        assertEquals(bookings, result);
        verify(bookingRepo).findAll();
    }

    @Test
    void cancelBooking_success() {
        TableBooking booking = new TableBooking();
        booking.setStatus(TableBooking.BookingStatus.BOOKED);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(TableBooking.class))).thenReturn(booking);

        bookingService.cancelBooking(1L);

        assertEquals(TableBooking.BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepo).save(booking);
    }

    @Test
    void cancelBooking_notFound() {
        when(bookingRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking(2L));
    }
}
