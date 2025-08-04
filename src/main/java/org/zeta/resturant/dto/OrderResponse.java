package org.zeta.resturant.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private long orderId;
    private String orderStatus;
    private LocalDateTime createdAt;

    private int tableNumber;
    private int tableCapacity;

    private String customerName;
    private String customerPhone;
    private LocalDateTime bookingTime;

    private List<OrderItemResponse> items;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getTableCapacity() {
        return tableCapacity;
    }

    public void setTableCapacity(int tableCapacity) {
        this.tableCapacity = tableCapacity;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public OrderResponse(long orderId, String orderStatus, LocalDateTime createdAt,
                         int tableNumber, int tableCapacity,
                         String customerName, String customerPhone, LocalDateTime bookingTime,
                         List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.tableNumber = tableNumber;
        this.tableCapacity = tableCapacity;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.bookingTime = bookingTime;
        this.items = items;
    }

    // Add getters and setters if needed
}


