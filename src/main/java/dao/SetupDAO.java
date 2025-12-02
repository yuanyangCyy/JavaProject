package dao;

import model.VehicleCategory;
import util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO responsible for Database Setup and Resetting.
 */
public class SetupDAO {

    /**
     * 1. Automatic Table Creation Method
     * Checks if the required tables exist in the database and creates them if they do not.
     * This fulfills the project documentation requirement for automatic table creation upon valid login.
     */
    public void createTables() {
        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("--- Checking/Creating Tables ---");

            // 1. Create Vehicle Table (if not exists)
            String sqlVehicle = "CREATE TABLE IF NOT EXISTS rental_vehicles (" +
                    "type VARCHAR(50) PRIMARY KEY, " +
                    "daily_rate DOUBLE, " +
                    "image_path VARCHAR(255))";
            stmt.execute(sqlVehicle);

            // 2. Create Customer Table (if not exists)
            String sqlCustomer = "CREATE TABLE IF NOT EXISTS rental_customers (" +
                    "customer_id VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "phone VARCHAR(20), " +
                    "email VARCHAR(100))";
            stmt.execute(sqlCustomer);

            // 3. Create Booking Table (if not exists) - With Foreign Key Constraints
            String sqlBooking = "CREATE TABLE IF NOT EXISTS rental_bookings (" +
                    "booking_id VARCHAR(50) PRIMARY KEY, " +
                    "customer_id VARCHAR(50), " +
                    "vehicle_type VARCHAR(50), " +
                    "start_date DATE, " +
                    "duration INT, " +
                    "total_price DOUBLE, " +
                    "FOREIGN KEY (customer_id) REFERENCES rental_customers(customer_id), " +
                    "FOREIGN KEY (vehicle_type) REFERENCES rental_vehicles(type))";
            stmt.execute(sqlBooking);

            System.out.println("-> Tables Verified/Created Successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * 2. Reset Database Method
     * Wipes all existing data and re-inserts the default vehicle types from the Enum.
     * WARNING: This deletes all existing customers and bookings!
     */
    public void resetDatabase() {
        try (Connection conn = DBConnectionManager.getConnection()) {
            if (conn == null) return;

            Statement stmt = conn.createStatement();

            // 1. Temporarily disable foreign key checks to allow truncation
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            System.out.println("--- Clearing Old Data ---");
            try {
                // Clear all tables
                stmt.execute("TRUNCATE TABLE rental_bookings");
                stmt.execute("TRUNCATE TABLE rental_customers");
                stmt.execute("TRUNCATE TABLE rental_vehicles");
                System.out.println("-> Tables Cleared.");
            } catch (SQLException ex) {
                // If tables do not exist yet (rare case), skip the truncate step
                System.out.println("Skipping truncate (tables might be new).");
            }

            // 2. Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            // 3. Inject default vehicle data from the VehicleCategory Enum
            System.out.println("--- Injecting Default Vehicles ---");
            String insertSQL = "INSERT INTO rental_vehicles (type, daily_rate, image_path) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (VehicleCategory cat : VehicleCategory.values()) {
                    pstmt.setString(1, cat.getLabel());
                    pstmt.setDouble(2, cat.getRate());
                    pstmt.setString(3, cat.getImageUrl());
                    pstmt.executeUpdate();
                    System.out.println("-> Inserted: " + cat.getLabel());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}