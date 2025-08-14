package org.zeta.resturant.exceptions;

public class ActiveBookingNotFoundException extends RuntimeException {
    public ActiveBookingNotFoundException(String message) {
        super(message);
    }
}
