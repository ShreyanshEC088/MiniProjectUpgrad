package org.zeta.resturant.model;

import java.time.LocalDateTime;

public class KitchenOrder {
    private long id;
    private long orderId;
    private int tableNumber;
    private String itemName;
    private int quantity;

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

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private String status;
    private LocalDateTime createdAt;

    public KitchenOrder(long id, long orderId, int tableNumber, String itemName,
                        int quantity, String status, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.itemName = itemName;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and setters...
}

