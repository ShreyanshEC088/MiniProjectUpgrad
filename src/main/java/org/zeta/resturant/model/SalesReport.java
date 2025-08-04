package org.zeta.resturant.model;

import java.time.LocalDate;
import java.util.List;

public class SalesReport {
    private LocalDate date;
    private int totalOrders;

    public List<MenuItemSales> getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(List<MenuItemSales> itemsSold) {
        this.itemsSold = itemsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    private double totalRevenue;
    private List<MenuItemSales> itemsSold;

}

