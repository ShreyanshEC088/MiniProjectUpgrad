package org.zeta.resturant.model;

import org.zeta.resturant.enums.BookingStatus;

import java.time.LocalDateTime;

public class Booking {

    private long id;
    private String customerName;
    private String phoneNumber;
    private LocalDateTime bookingTime;
    private BookingStatus status;
    private Table table;
    private int tableNumber;

    public Booking(long id, String customerName, String phoneNumber, LocalDateTime bookingTime, int tableNumber,
                   BookingStatus status, Table table) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.bookingTime = bookingTime;
        this.tableNumber = tableNumber;
        this.status = status;
        this.table = table;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
