package org.zeta.resturant.dao;

import org.zeta.resturant.config.DbConfig;
import org.zeta.resturant.model.Bill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BillDao {

    private static final String URL = DbConfig.getUrl();
    private static final String USER = DbConfig.getUser();
    private static final String PASSWORD = DbConfig.getPassword();

    private static final String INSERT_SQL =
            "INSERT INTO bills (order_id, total_amount, is_paid, created_at) VALUES (?, ?, ?, ?) RETURNING id";

    private static final String FETCH_BY_ORDER_ID =
            "SELECT * FROM bills WHERE order_id = ?";

    private static final String UPDATE_PAYMENT_STATUS =
            "UPDATE bills SET is_paid = true WHERE id = ?";

    public long generateBill(Bill bill) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setLong(1, bill.getOrderId());
            stmt.setDouble(2, bill.getTotalAmount());
            stmt.setBoolean(3, bill.isPaid());
            stmt.setTimestamp(4, Timestamp.valueOf(bill.getCreatedAt()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Bill getBillByOrderId(long orderId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(FETCH_BY_ORDER_ID)) {

            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Bill(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getDouble("total_amount"),
                        rs.getBoolean("is_paid"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markAsPaid(long billId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PAYMENT_STATUS)) {

            stmt.setLong(1, billId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

