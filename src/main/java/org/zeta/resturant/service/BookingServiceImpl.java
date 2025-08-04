package org.zeta.resturant.service;

import org.zeta.resturant.dao.BookingDao;
import org.zeta.resturant.dto.BookingRequest;
import org.zeta.resturant.dto.BookingResponse;
import org.zeta.resturant.enums.BookingStatus;
import org.zeta.resturant.model.Booking;
import org.zeta.resturant.model.Table;
import java.util.*;
import java.time.LocalDateTime;

public class BookingServiceImpl implements BookingService{

    private BookingDao bookingDao = new BookingDao();

    @Override
    public BookingResponse bookTable(BookingRequest request) {
        if (bookingDao.isDuplicateBooking(request.getCustomerName(), request.getPhoneNumber(), request.getTableNumber())) {
            return new BookingResponse(-1, "Duplicate booking not allowed.");
        }
        Table table = bookingDao.getTableByNumber(request.getTableNumber());
        if (table == null) {
            return new BookingResponse(-1, "Table not found.");
        }
        if (!table.isAvailable()) {
            return new BookingResponse(-1, "Table is already booked.");
        }

        Booking booking = new Booking(
                0L,
                request.getCustomerName(),
                request.getPhoneNumber(),
                LocalDateTime.now(),
                request.getTableNumber(),
                BookingStatus.BOOKED,
                table
        );

        bookingDao.save(booking);
        bookingDao.markTableAsUnavailable(request.getTableNumber());

        return new BookingResponse((int) booking.getId(), "Booking confirmed");
    }



    @Override
    public List<BookingResponse> getAllBookings() {
        try {
            List<Booking> bookings = bookingDao.getAll();
            List<BookingResponse> responses = new ArrayList<>();
            for (Booking booking : bookings) {
                responses.add(new BookingResponse(
                        (int) booking.getId(),
                        "Booking for " + booking.getCustomerName() + " on table " + booking.getTableNumber()
                ));
            }
            return responses;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(new BookingResponse(-1, "Error fetching bookings"));
        }
    }

}