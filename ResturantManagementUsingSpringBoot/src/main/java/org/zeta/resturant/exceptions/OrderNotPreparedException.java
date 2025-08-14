package org.zeta.resturant.exceptions;

public class OrderNotPreparedException extends RuntimeException {
    public OrderNotPreparedException(String message) {
        super(message);
    }
}
