package org.zeta.resturant.service;

import org.zeta.resturant.dto.BookingRequest;
import org.zeta.resturant.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse bookTable(BookingRequest request);
    List<BookingResponse> getAllBookings();

}
