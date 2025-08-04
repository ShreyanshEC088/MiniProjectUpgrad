package org.zeta.resturant.dto;

public class BookingRequest {
    private String customerName;
    private String phoneNumber;
    private int tableNumber;

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getTableNumber() {
        return tableNumber;
    }
}
