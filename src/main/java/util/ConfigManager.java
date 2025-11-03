package util;

import java.io.*;
import java.util.Properties;


public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;

    public ConfigManager() {
        this.properties = new Properties();
    }

    /**
     * Saves the given database connection details to the config.properties file.
     *
     * @param url      The JDBC URL.
     * @param user     The database username.
     * @param password The database password.
     * @return true if save was successful, false otherwise.
     */
    public boolean saveConfig(String url, String user, String password) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.setProperty("db.url", url);
            properties.setProperty("db.user", user);
            properties.setProperty("db.password", password);
            properties.store(output, "Car Rental App DB Configuration");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving config file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads the database connection properties from the config.properties file.
     *
     * @return The loaded Properties object, or null if an error occurred.
     */
    public Properties loadConfig() {
        if (!configExists()) {
            return null;
        }
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if the config.properties file exists.
     *
     * @return true if the file exists, false otherwise.
     */
    public boolean configExists() {
        return new java.io.File(CONFIG_FILE).exists();
    }
}