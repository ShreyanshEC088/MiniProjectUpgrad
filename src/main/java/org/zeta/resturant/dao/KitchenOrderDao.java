package org.zeta.resturant.dao;


import org.zeta.resturant.config.DbConfig;
import org.zeta.resturant.model.KitchenOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class KitchenOrderDao {
    private static final String URL = DbConfig.getUrl();
    private static final String USER = DbConfig.getUser();
    private static final String PASSWORD = DbConfig.getPassword();

    public List<KitchenOrder> getLiveKitchenOrders() {
        String query = "SELECT * FROM kitchen_orders WHERE status != 'PREPARED' ORDER BY created_at";
        List<KitchenOrder> orders = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KitchenOrder order = new KitchenOrder(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getInt("table_number"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public boolean markAsPrepared(long kitchenOrderId) {
        String update = "UPDATE kitchen_orders SET status = 'PREPARED' WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setLong(1, kitchenOrderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
