package org.zeta.resturant.dto;

public class BookingResponse {
    private int bookingId;
    private String message;

    public BookingResponse(int bookingId, String message) {
        this.bookingId = bookingId;
        this.message = message;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getMessage() {
        return message;
    }
}
