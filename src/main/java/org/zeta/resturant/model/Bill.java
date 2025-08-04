package org.zeta.resturant.model;

import java.time.LocalDateTime;

public class Bill {
    private long id;
    private long orderId;
    private double totalAmount;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private boolean isPaid;
    private LocalDateTime createdAt;

    public Bill(long id, long orderId, double totalAmount, boolean isPaid, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.isPaid = isPaid;
        this.createdAt = createdAt;
    }

}

