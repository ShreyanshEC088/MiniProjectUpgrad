package org.zeta.resturant.exceptions;

public class TableOccupiedException extends RuntimeException {
    public TableOccupiedException(String message) {
        super(message);
    }
}
