package org.zeta.resturant.model;

import org.zeta.resturant.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private long id;
    private Table table;
    private Booking booking;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItem> orderItems;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Order(long id, Table table, Booking booking, LocalDateTime createdAt,
                 OrderStatus status, List<OrderItem> orderItems) {
        this.id = id;
        this.table = table;
        this.booking = booking;
        this.createdAt = createdAt;
        this.status = status;
        this.orderItems = orderItems;
    }
}
