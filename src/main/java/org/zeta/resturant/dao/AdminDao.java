package org.zeta.resturant.dao;

import org.zeta.resturant.config.DbConfig;
import org.zeta.resturant.model.MenuItemSales;
import org.zeta.resturant.model.SalesReport;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    private static final String URL = DbConfig.getUrl();
    private static final String USER = DbConfig.getUser();
    private static final String PASSWORD = DbConfig.getPassword();

    public boolean updateMenuItemPrice(long itemId, double newPrice) {
        String query = "UPDATE menu_items SET price = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, newPrice);
            stmt.setLong(2, itemId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SalesReport getDailySalesReport(LocalDate date) {
        String query = """
            SELECT mi.name, SUM(oi.quantity) AS total_quantity, 
                   SUM(oi.quantity * mi.price) AS total_revenue
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN menu_items mi ON oi.menu_item_id = mi.id
            WHERE DATE(o.created_at) = ?
            GROUP BY mi.name
        """;

        SalesReport report = new SalesReport();
        report.setDate(date);
        List<MenuItemSales> items = new ArrayList<>();
        double totalRevenue = 0;
        int totalOrders = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MenuItemSales item = new MenuItemSales(
                        rs.getString("name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_revenue")
                );
                items.add(item);
                totalRevenue += item.getTotalAmount();
            }

            totalOrders = getOrderCountByDate(conn, date);
            report.setItemsSold(items);
            report.setTotalOrders(totalOrders);
            report.setTotalRevenue(totalRevenue);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report;
    }

    private int getOrderCountByDate(Connection conn, LocalDate date) throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(countQuery)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}

