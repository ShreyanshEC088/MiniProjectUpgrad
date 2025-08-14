package org.zeta.resturant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeta.resturant.dao.OrderRepository;
import org.zeta.resturant.dao.TableBookingRepository;
import org.zeta.resturant.exceptions.BookingNotFoundException;
import org.zeta.resturant.exceptions.TableOccupiedException;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.TableBooking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private TableBookingRepository bookingRepo;

    @Autowired
    private OrderRepository orderRepo;

    public boolean isTableAvailable(int tableNumber) {
        List<Order> activeOrders = orderRepo.findByTableNumberAndStatusNot(tableNumber, Order.OrderStatus.PAID);
        List<TableBooking> activeBookings = bookingRepo.findByTableNumberAndStatus(tableNumber, TableBooking.BookingStatus.BOOKED);
        return activeOrders.isEmpty() && activeBookings.isEmpty();
    }

    public TableBooking bookTable(TableBooking booking) {
        if (!isTableAvailable(booking.getTableNumber())) {
            throw new TableOccupiedException(
                    "Table " + booking.getTableNumber() + " is currently occupied or booked."
            );
        }
        booking.setStatus(TableBooking.BookingStatus.BOOKED);
        booking.setBookingTime(LocalDateTime.now());
        return bookingRepo.save(booking);
    }

    public List<TableBooking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public void cancelBooking(Long id) {
        TableBooking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));
        booking.setStatus(TableBooking.BookingStatus.CANCELLED);
        bookingRepo.save(booking);
    }
}

