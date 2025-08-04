package org.zeta.resturant.model;

public class MenuItemSales {
    private String itemName;
    private int quantitySold;
    private double totalAmount;

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public MenuItemSales(String itemName, int quantitySold, double totalAmount) {
        this.itemName = itemName;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
    }
}
