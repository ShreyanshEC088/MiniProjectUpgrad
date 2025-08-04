package org.zeta.resturant.dao;

import org.zeta.resturant.enums.BookingStatus;
import org.zeta.resturant.enums.OrderStatus;
import org.zeta.resturant.model.*;
import org.zeta.resturant.config.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDao {

    private static final String URL = DbConfig.getUrl();
    private static final String USER = DbConfig.getUser();
    private static final String PASSWORD = DbConfig.getPassword();

    public long saveOrder(Order order) {
        long orderId = -1;
        String orderSql = "INSERT INTO orders (table_number, booking_id, created_at, status) VALUES (?, ?, ?, ?) RETURNING id";
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
                orderStmt.setInt(1, order.getTable().getTableNumber());
                orderStmt.setLong(2, order.getBooking().getId());
                orderStmt.setTimestamp(3, Timestamp.valueOf(order.getCreatedAt()));
                orderStmt.setString(4, order.getStatus().name());

                ResultSet rs = orderStmt.executeQuery();
                if (rs.next()) {
                    orderId = rs.getLong("id");
                }
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (OrderItem item : order.getOrderItems()) {
                    itemStmt.setLong(1, orderId);
                    itemStmt.setLong(2, item.getMenuItem().getId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderId;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String orderSql = "SELECT o.id AS order_id, o.table_number, o.booking_id, o.created_at, o.status, " +
                "t.id AS table_id, t.capacity, t.is_available, " +
                "b.customer_name, b.phone_number, b.booking_time, b.status AS booking_status, " +
                "oi.id AS order_item_id, oi.menu_item_id, oi.quantity, " +
                "m.name AS menu_name, m.description, m.price, m.is_available AS menu_available " +
                "FROM orders o " +
                "JOIN tables t ON o.table_number = t.table_number " +
                "JOIN bookings b ON o.booking_id = b.id " +
                "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                "LEFT JOIN menu_items m ON oi.menu_item_id = m.id " +
                "ORDER BY o.id";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(orderSql);
             ResultSet rs = stmt.executeQuery()) {

            Map<Long, Order> orderMap = new LinkedHashMap<>();

            while (rs.next()) {
                long orderId = rs.getLong("order_id");
                Order order = orderMap.get(orderId);

                if (order == null) {
                    Table table = new Table(
                            rs.getLong("table_id"),
                            rs.getInt("table_number"),
                            rs.getInt("capacity"),
                            rs.getBoolean("is_available")
                    );

                    Booking booking = new Booking(
                            rs.getLong("booking_id"),
                            rs.getString("customer_name"),
                            rs.getString("phone_number"),
                            rs.getTimestamp("booking_time").toLocalDateTime(),
                            rs.getInt("table_number"),
                            BookingStatus.valueOf(rs.getString("booking_status")),
                            table
                    );

                    order = new Order(
                            orderId,
                            table,
                            booking,
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            OrderStatus.valueOf(rs.getString("status")),
                            new ArrayList<>()
                    );

                    orderMap.put(orderId, order);
                }

                long itemId = rs.getLong("order_item_id");
                if (itemId != 0) {
                    MenuItem menuItem = new MenuItem(
                            rs.getLong("menu_item_id"),
                            rs.getString("menu_name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getBoolean("menu_available")
                    );
                    OrderItem orderItem = new OrderItem(itemId, order, menuItem, rs.getInt("quantity"));
                    order.getOrderItems().add(orderItem);
                }
            }

            orders.addAll(orderMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public Order getOrderById(long orderId) {
        String orderQuery = "SELECT * FROM orders WHERE id = ?";
        String itemQuery = "SELECT oi.*, mi.name AS menu_name, mi.description AS menu_description, mi.price AS menu_price, mi.is_available " +
                "FROM order_items oi JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                "WHERE oi.order_id = ?";
        String bookingQuery = "SELECT b.*, t.id as table_id, t.table_number, t.capacity, t.is_available " +
                "FROM bookings b JOIN tables t ON b.table_number = t.table_number WHERE b.id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Order order = null;
            try (PreparedStatement stmt = conn.prepareStatement(orderQuery)) {
                stmt.setLong(1, orderId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    order = new Order(
                            rs.getLong("id"),
                            null,
                            null,
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            OrderStatus.valueOf(rs.getString("status")),
                            new ArrayList<>()
                    );
                    order.setId(rs.getLong("id"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                    order.setOrderItems(new ArrayList<>());

                    long bookingId = rs.getLong("booking_id");
                    try (PreparedStatement bStmt = conn.prepareStatement(bookingQuery)) {
                        bStmt.setLong(1, bookingId);
                        ResultSet brs = bStmt.executeQuery();
                        if (brs.next()) {
                            Table table = new Table(
                                    brs.getLong("table_id"),
                                    brs.getInt("table_number"),
                                    brs.getInt("capacity"),
                                    brs.getBoolean("is_available")
                            );

                            Booking booking = new Booking(
                                    brs.getLong("id"),
                                    brs.getString("customer_name"),
                                    brs.getString("phone_number"),
                                    brs.getTimestamp("booking_time").toLocalDateTime(),
                                    brs.getInt("table_id"),
                                    BookingStatus.valueOf(brs.getString("status")),
                                    table
                            );

                            order.setBooking(booking);
                            order.setTable(table);
                        }
                    }

                    try (PreparedStatement iStmt = conn.prepareStatement(itemQuery)) {
                        iStmt.setLong(1, orderId);
                        ResultSet irs = iStmt.executeQuery();
                        while (irs.next()) {
                            MenuItem item = new MenuItem(
                                    irs.getLong("menu_item_id"),
                                    irs.getString("menu_name"),
                                    irs.getString("menu_description"),
                                    irs.getDouble("menu_price"),
                                    irs.getBoolean("is_available")
                            );
                            OrderItem orderItem = new OrderItem(
                                    irs.getLong("id"),
                                    order,
                                    item,
                                    irs.getInt("quantity")
                            );
                            order.getOrderItems().add(orderItem);
                        }
                    }
                }
            }

            return order;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Table getTableByNumber(int tableNumber) {
        String query = "SELECT * FROM tables WHERE table_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, tableNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Table(
                            rs.getLong("id"),
                            rs.getInt("table_number"),
                            rs.getInt("capacity"),
                            rs.getBoolean("is_available")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void markTableAsUnavailable(long tableId) {
        String query = "UPDATE tables SET is_available = false WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, tableId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markTableAsAvailable(long tableId) {
        String query = "UPDATE tables SET is_available = true WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, tableId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
