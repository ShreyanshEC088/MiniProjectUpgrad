package org.zeta.resturant.service;

import org.zeta.resturant.config.DbConfig;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.OrderItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class KitchenOrderNotifier {

    private static final String url = DbConfig.getUrl();
    private static final String user = DbConfig.getUser();
    private static final String password = DbConfig.getPassword();

    private static final String INSERT_SQL =
            "INSERT INTO kitchen_orders (order_id, table_number, item_name, quantity, status, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    public void notifyKitchen(Order order) {

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            for (OrderItem item : order.getOrderItems()) {
                stmt.setLong(1, order.getId());
                stmt.setInt(2, order.getTable().getTableNumber());
                stmt.setString(3, item.getMenuItem().getName());
                stmt.setInt(4, item.getQuantity());
                stmt.setString(5, "PENDING");
                stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Order sent to kitchen system.");

        } catch (SQLException e) {
            throw new RuntimeException("Error sending order to kitchen", e);
        }
    }
}

