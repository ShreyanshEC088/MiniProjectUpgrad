package org.zeta.resturant.dto;

import java.util.List;

public class OrderRequest {
    private int tableNumber;
    private String customerName;
    private String customerPhone;
    private List<OrderItemRequest> items;

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }
}
