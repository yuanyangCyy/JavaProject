package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionManager {

    private static Properties config;
    private static Connection connection = null;

    public static boolean loadConfig() {
        ConfigManager configManager = new ConfigManager();
        config = configManager.loadConfig();
        return config != null;
    }

    public static String getDbUrl() {
        return (config != null) ? config.getProperty("db.url") : "";
    }

    public static Connection getConnection() throws SQLException {
        if (config == null) {
            if (!loadConfig()) {
                throw new SQLException("Configuration not loaded or found.");
            }
        }

        String url = config.getProperty("db.url");
        String user = config.getProperty("db.user");
        String password = config.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            System.err.println("Connection Failed: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}