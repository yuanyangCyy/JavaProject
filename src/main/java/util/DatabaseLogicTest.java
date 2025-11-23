package util;

import java.sql.*;
import java.time.LocalDate;

/**
 * DatabaseLogicTest
 * * This class is designed to test the core logic of the Rental Car System
 * directly against the database. It performs the following operations:
 * 1. Setup: Creates necessary tables (rental_vehicles, rental_customers, rental_bookings).
 * 2. Create: Inserts sample data for vehicles, customers, and bookings.
 * 3. Read: Retrieves and displays data to verify insertion.
 * 4. Update: Modifies an existing booking.
 * 5. Delete: Removes a booking to verify deletion logic.
 * * Usage: Run the main() method to execute the test suite.
 */
public class DatabaseLogicTest {

    public static void main(String[] args) {
        System.out.println("=== Rental System Database Logic Test Start ===");

        // 1. Load Database Configuration
        // Using the existing DBConnectionManager to load config.properties
        if (!DBConnectionManager.loadConfig()) {
            System.err.println("Error: Failed to load config.properties. Please check the file path.");
            return;
        }
        System.out.println("Configuration Loaded: " + DBConnectionManager.getDbUrl());

        // 2. Establish Connection
        try (Connection conn = DBConnectionManager.getConnection()) {

            if (conn == null) {
                System.err.println("Error: Connection object is null.");
                return;
            }
            System.out.println("Connection Established Successfully.\n");

            // --- STEP 1: INITIALIZE DATABASE (RESET TABLES) ---
            setupTables(conn);

            // --- STEP 2: CREATE DATA (INSERT) ---
            // Insert a Vehicle
            String vehicleType = "Small Sedan";
            createVehicle(conn, vehicleType, 45.00, "sedan.png");

            // Insert a Customer
            String customerId = "CUST-001";
            createCustomer(conn, customerId, "Test User", "123-456-7890", "test@email.com");

            // Insert a Booking (Linking Customer and Vehicle)
            String bookingId = "BOOK-100";
            createBooking(conn, bookingId, customerId, vehicleType, 3); // Rent for 3 days

            // --- STEP 3: READ DATA (SELECT) ---
            System.out.println("\n--- Reading Current Data ---");
            printTableData(conn, "rental_bookings");

            // --- STEP 4: UPDATE DATA (MODIFY) ---
            // Extend the rental duration to 5 days
            updateBookingDuration(conn, bookingId, 5, 45.00);

            // --- STEP 5: DELETE DATA (REMOVE) ---
            // Delete the booking
            deleteBooking(conn, bookingId);

            // --- STEP 6: VERIFY INTEGRITY ---
            // Check if customer still exists after booking deletion
            verifyCustomerExists(conn, customerId);

        } catch (SQLException e) {
            System.err.println("!!! SQL Exception Occurred !!!");
            e.printStackTrace();
        }

        System.out.println("\n=== Rental System Database Logic Test End ===");
    }

    /**
     * Drops existing tables and creates new ones to ensure a clean testing environment.
     * Note: Tables are created with 'rental_' prefix to avoid conflicts.
     */
    private static void setupTables(Connection conn) throws SQLException {
        System.out.println("--- Setup: Initializing Tables ---");
        Statement stmt = conn.createStatement();

        // Drop tables in correct order (Child first, then Parent) to avoid Foreign Key constraints
        stmt.execute("DROP TABLE IF EXISTS rental_bookings");
        stmt.execute("DROP TABLE IF EXISTS rental_customers");
        stmt.execute("DROP TABLE IF EXISTS rental_vehicles");

        // 1. Create Vehicle Table
        String sqlVehicle = "CREATE TABLE rental_vehicles (" +
                "type VARCHAR(50) PRIMARY KEY, " +
                "daily_rate DOUBLE, " +
                "image_path VARCHAR(255))";
        stmt.execute(sqlVehicle);

        // 2. Create Customer Table
        String sqlCustomer = "CREATE TABLE rental_customers (" +
                "customer_id VARCHAR(50) PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "phone VARCHAR(20), " +
                "email VARCHAR(100))";
        stmt.execute(sqlCustomer);

        // 3. Create Booking Table (With Foreign Keys)
        String sqlBooking = "CREATE TABLE rental_bookings (" +
                "booking_id VARCHAR(50) PRIMARY KEY, " +
                "customer_id VARCHAR(50), " +
                "vehicle_type VARCHAR(50), " +
                "start_date DATE, " +
                "duration INT, " +
                "total_price DOUBLE, " +
                "FOREIGN KEY (customer_id) REFERENCES rental_customers(customer_id), " +
                "FOREIGN KEY (vehicle_type) REFERENCES rental_vehicles(type))";
        stmt.execute(sqlBooking);

        System.out.println("-> Tables created successfully.");
    }

    /**
     * Inserts a new vehicle type into the database.
     */
    private static void createVehicle(Connection conn, String type, double rate, String imgPath) throws SQLException {
        String sql = "INSERT INTO rental_vehicles (type, daily_rate, image_path) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setDouble(2, rate);
            pstmt.setString(3, imgPath);
            pstmt.executeUpdate();
            System.out.println("-> Inserted Vehicle: " + type);
        }
    }

    /**
     * Inserts a new customer into the database.
     */
    private static void createCustomer(Connection conn, String id, String name, String phone, String email) throws SQLException {
        String sql = "INSERT INTO rental_customers (customer_id, name, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            System.out.println("-> Inserted Customer: " + name);
        }
    }

    /**
     * Creates a booking record. Calculates total price based on daily rate.
     */
    private static void createBooking(Connection conn, String bookingId, String customerId, String vehicleType, int days) throws SQLException {
        // First, get the daily rate for the vehicle to calculate total price
        // (Simplified logic: Passing rate is safer, but here we query or assume consistent logic)
        // For this test, we assume the rate is known or fetched. Let's fetch it for realism.

        double dailyRate = 0;
        try (PreparedStatement p = conn.prepareStatement("SELECT daily_rate FROM rental_vehicles WHERE type = ?")) {
            p.setString(1, vehicleType);
            ResultSet rs = p.executeQuery();
            if (rs.next()) dailyRate = rs.getDouble("daily_rate");
        }

        double totalPrice = dailyRate * days;
        LocalDate startDate = LocalDate.now().plusDays(1); // Start tomorrow

        String sql = "INSERT INTO rental_bookings (booking_id, customer_id, vehicle_type, start_date, duration, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookingId);
            pstmt.setString(2, customerId);
            pstmt.setString(3, vehicleType);
            pstmt.setDate(4, Date.valueOf(startDate));
            pstmt.setInt(5, days);
            pstmt.setDouble(6, totalPrice);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> Created Booking: " + bookingId + " [Total: $" + totalPrice + "]");
            }
        }
    }

    /**
     * Updates an existing booking's duration and recalculates the price.
     */
    private static void updateBookingDuration(Connection conn, String bookingId, int newDays, double dailyRate) throws SQLException {
        System.out.println("\n--- Updating Booking ---");
        double newTotal = dailyRate * newDays;

        String sql = "UPDATE rental_bookings SET duration = ?, total_price = ? WHERE booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newDays);
            pstmt.setDouble(2, newTotal);
            pstmt.setString(3, bookingId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> SUCCESS: Updated Booking " + bookingId + " to " + newDays + " days.");
            } else {
                System.out.println("-> FAIL: Booking not found.");
            }
        }
    }

    /**
     * Deletes a booking from the database.
     */
    private static void deleteBooking(Connection conn, String bookingId) throws SQLException {
        System.out.println("\n--- Deleting Booking ---");
        String sql = "DELETE FROM rental_bookings WHERE booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookingId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> SUCCESS: Deleted Booking " + bookingId);
            } else {
                System.out.println("-> FAIL: Could not delete (ID not found).");
            }
        }
    }

    /**
     * Verifies that the customer data remains intact.
     */
    private static void verifyCustomerExists(Connection conn, String customerId) throws SQLException {
        System.out.println("\n--- Verifying Customer Integrity ---");
        String sql = "SELECT name FROM rental_customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("-> VALID: Customer '" + rs.getString("name") + "' still exists in DB.");
            } else {
                System.err.println("-> ERROR: Customer missing!");
            }
        }
    }

    /**
     * Utility method to print all rows from a table to the console.
     */
    private static void printTableData(Connection conn, String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        while (rs.next()) {
            StringBuilder row = new StringBuilder("| ");
            for (int i = 1; i <= colCount; i++) {
                row.append(meta.getColumnName(i)).append(": ").append(rs.getString(i)).append(" | ");
            }
            System.out.println(row.toString());
        }
    }
}