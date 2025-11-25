package dao;

import model.Booking;
import model.Customer;
import model.VehicleType;
import util.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    /**
     * Creates a booking and returns the generated Booking ID.
     * Returns NULL if operation fails.
     */
    public String createBooking(String customerName, String phone, String email, VehicleType vehicle, LocalDate date, int days) {
        Connection conn = null;
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false); // Transaction start

            // 1. Generate IDs
            String custId = "C-" + System.currentTimeMillis();
            String bookingId = "B-" + System.currentTimeMillis(); // We need to return this

            // 2. Insert Customer
            String sqlCust = "INSERT INTO rental_customers (customer_id, name, phone, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCust)) {
                ps.setString(1, custId);
                ps.setString(2, customerName);
                ps.setString(3, phone);
                ps.setString(4, email);
                ps.executeUpdate();
            }

            // 3. Insert Booking
            double total = vehicle.getDailyRate() * days;
            String sqlBook = "INSERT INTO rental_bookings (booking_id, customer_id, vehicle_type, start_date, duration, total_price) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlBook)) {
                ps.setString(1, bookingId);
                ps.setString(2, custId);
                ps.setString(3, vehicle.getType());
                ps.setDate(4, Date.valueOf(date));
                ps.setInt(5, days);
                ps.setDouble(6, total);
                ps.executeUpdate();
            }

            conn.commit(); // Transaction end
            return bookingId; // SUCCESS: Return the ID string

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return null; // FAIL
        }
    }

    // --- The rest of the methods remain unchanged (find, update, delete, getAll) ---
    // (Ensure you keep findBookingById, updateBookingDuration, deleteBooking, getAllBookings here as they were)

    public Booking findBookingById(String bookingId) {
        String sql = "SELECT b.*, c.name, c.phone, c.email, v.daily_rate, v.image_path " +
                "FROM rental_bookings b " +
                "JOIN rental_customers c ON b.customer_id = c.customer_id " +
                "JOIN rental_vehicles v ON b.vehicle_type = v.type " +
                "WHERE b.booking_id = ?";
        try (Connection conn = DBConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer c = new Customer(rs.getString("customer_id"), rs.getString("name"), rs.getString("phone"), rs.getString("email"));
                VehicleType v = new VehicleType(rs.getString("vehicle_type"), rs.getDouble("daily_rate"), rs.getString("image_path"));
                return new Booking(rs.getString("booking_id"), c, v, rs.getDate("start_date").toLocalDate(), rs.getInt("duration"), rs.getDouble("total_price"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateBookingDuration(String bookingId, int newDuration) {
        Booking b = findBookingById(bookingId);
        if (b == null) return false;
        double newTotal = b.getVehicleType().getDailyRate() * newDuration;
        String sql = "UPDATE rental_bookings SET duration = ?, total_price = ? WHERE booking_id = ?";
        try (Connection conn = DBConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newDuration);
            ps.setDouble(2, newTotal);
            ps.setString(3, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deleteBooking(String bookingId) {
        String sql = "DELETE FROM rental_bookings WHERE booking_id = ?";
        try (Connection conn = DBConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, c.name, c.phone, c.email, v.daily_rate, v.image_path " +
                "FROM rental_bookings b " +
                "JOIN rental_customers c ON b.customer_id = c.customer_id " +
                "JOIN rental_vehicles v ON b.vehicle_type = v.type";
        try (Connection conn = DBConnectionManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                Customer c = new Customer(rs.getString("customer_id"), rs.getString("name"), rs.getString("phone"), rs.getString("email"));
                VehicleType v = new VehicleType(rs.getString("vehicle_type"), rs.getDouble("daily_rate"), rs.getString("image_path"));
                list.add(new Booking(rs.getString("booking_id"), c, v, rs.getDate("start_date").toLocalDate(), rs.getInt("duration"), rs.getDouble("total_price")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}