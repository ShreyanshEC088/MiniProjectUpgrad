package org.zeta.resturant.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.zeta.resturant.config.DbConfig;
import org.zeta.resturant.enums.BookingStatus;
import org.zeta.resturant.model.Booking;
import org.zeta.resturant.model.Table;

public class BookingDao {
    private static final String URL = DbConfig.getUrl();
    private static final String USER = DbConfig.getUser();
    private static final String PASSWORD = DbConfig.getPassword();

    public Booking save(Booking booking) {
        String sql = "INSERT INTO bookings (customer_name, phone_number, table_number, booking_time, status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getCustomerName());
            stmt.setString(2, booking.getPhoneNumber());
            stmt.setInt(3, booking.getTableNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getBookingTime()));
            stmt.setString(5, booking.getStatus().name());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                booking.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public boolean isDuplicateBooking(String customerName, String phoneNumber, int tableNumber) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE customer_name = ? AND phone_number = ? AND table_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerName);
            stmt.setString(2, phoneNumber);
            stmt.setInt(3, tableNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Booking> getAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.id, b.customer_name, b.phone_number, b.booking_time, b.status, b.table_number, " +
                "t.id AS table_id, t.table_number AS t_number, t.capacity, t.is_available " +
                "FROM bookings b LEFT JOIN tables t ON b.table_number = t.table_number";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Table table = null;
                if (rs.getObject("table_id") != null) {
                    table = new Table(
                            rs.getLong("table_id"),
                            rs.getInt("t_number"),
                            rs.getInt("capacity"),
                            rs.getBoolean("is_available")
                    );
                }

                Timestamp timestamp = rs.getTimestamp("booking_time");
                LocalDateTime bookingTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;

                String statusStr = rs.getString("status");
                BookingStatus status = null;
                if (statusStr != null) {
                    try {
                        status = BookingStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException ex) {
                        System.err.println("Invalid booking status: " + statusStr);
                        status = BookingStatus.PENDING;
                    }
                } else {
                    status = BookingStatus.PENDING;
                }

                Booking booking = new Booking(
                        rs.getLong("id"),
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        bookingTime,
                        rs.getInt("table_number"),
                        status,
                        table
                );

                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public Table getTableByNumber(int tableNumber) {
        String sql = "SELECT * FROM tables WHERE table_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Table(
                        rs.getLong("id"),
                        rs.getInt("table_number"),
                        rs.getInt("capacity"),
                        rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void markTableAsUnavailable(int tableNumber) {
        String sql = "UPDATE tables SET is_available = false WHERE table_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markTableAsAvailable(int tableNumber) {
        String sql = "UPDATE tables SET is_available = true WHERE table_number = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}