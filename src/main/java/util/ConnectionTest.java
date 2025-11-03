package util;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    public static void main(String[] args) {
        System.out.println("=== Database Connection Test Start ===");
        if (!DBConnectionManager.loadConfig()) {
            System.err.println("Test FAILED: Failed to load config.properties!");
            System.err.println("Please ensure config.properties is in your project root directory (e.g., JavaProject [RentalCarSystem]).");
            return;
        }
        System.out.println("Configuration loaded: " + DBConnectionManager.getDbUrl());
        try (Connection conn = DBConnectionManager.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("******************************");
                System.out.println("Test SUCCESSFUL! Database connection established.");
                System.out.println("JDBC Driver loaded correctly.");
                System.out.println("******************************");
            } else {
                System.err.println("Test FAILED: Could not retrieve a valid connection.");
            }

        } catch (SQLException e) {
            System.err.println("!!!!! Test FAILED: SQL Exception occurred !!!!!");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Database Connection Test End ===");
    }
}

