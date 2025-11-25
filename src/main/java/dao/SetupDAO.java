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
     * Wipes all data and re-inserts the default vehicles from the Enum.
     * WARNING: This deletes all existing customers and bookings!
     */
    public void resetDatabase() {
        try (Connection conn = DBConnectionManager.getConnection()) {
            if (conn == null) return;

            // 1. Disable foreign keys to allow truncation
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // 2. Clear all tables
            System.out.println("--- Clearing Database ---");
            stmt.execute("TRUNCATE TABLE rental_bookings");
            stmt.execute("TRUNCATE TABLE rental_customers");
            stmt.execute("TRUNCATE TABLE rental_vehicles");
            System.out.println("-> Tables Cleared.");

            // 3. Re-enable foreign keys
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            // 4. Inject Vehicle Data from Enum
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