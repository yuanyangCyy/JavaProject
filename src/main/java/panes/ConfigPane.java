package panes;

import scenes.MenuScene;
import util.ConfigManager;
import util.DBConnectionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class ConfigPane extends BorderPane {

    private final TextField txtHost;
    private final TextField txtPort;
    private final TextField txtDbName;
    private final TextField txtUser;
    private final PasswordField txtPass;
    private final Label lblStatus;

    public ConfigPane() {
        // --- Layout Setup ---
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(500);

        // --- Header ---
        Label header = new Label("Database Configuration");
        header.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 24));
        header.setTextFill(AppConstants.PRIMARY_COLOR);

        Label subHeader = new Label("Please enter your database details to connect.");
        subHeader.setTextFill(AppConstants.GRAY_COLOR);

        // --- Form Fields ---
        // Defaults are set to help the user (localhost, 3306)
        txtHost = createStyledTextField("Host (e.g., localhost)");
        txtHost.setText("localhost");

        txtPort = createStyledTextField("Port (e.g., 3306 or 3307)");
        txtPort.setText("3307"); // Defaulting to your SSH port

        txtDbName = createStyledTextField("Database Name");
        txtDbName.setText("ychen1project"); // Default based on your config

        txtUser = createStyledTextField("Username");
        txtUser.setText("ychen1");

        txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        txtPass.setPrefHeight(40);

        // --- Connect Button ---
        Button btnConnect = new Button("Save & Connect");
        btnConnect.setPrefWidth(200);
        btnConnect.setPrefHeight(45);
        btnConnect.setBackground(new Background(AppConstants.primaryColorFill));
        btnConnect.setTextFill(Color.WHITE);
        btnConnect.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 14));

        // --- Status Label (For error messages) ---
        lblStatus = new Label();
        lblStatus.setTextFill(AppConstants.WARNING_COLOR);

        // --- Logic ---
        btnConnect.setOnAction(e -> handleConnection());

        // --- Add to Root ---
        root.getChildren().addAll(
                header, subHeader, new Separator(),
                new Label("Host:"), txtHost,
                new Label("Port:"), txtPort,
                new Label("Database Name:"), txtDbName,
                new Label("User:"), txtUser,
                new Label("Password:"), txtPass,
                new Separator(),
                lblStatus,
                btnConnect
        );

        setCenter(root);
        setBackground(new Background(AppConstants.backgroundColor));
    }

    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        return tf;
    }

    /**
     * Handles the logic to construct the URL, save to file, and test connection.
     */
    private void handleConnection() {
        String host = txtHost.getText().trim();
        String port = txtPort.getText().trim();
        String dbName = txtDbName.getText().trim();
        String user = txtUser.getText().trim();
        String pass = txtPass.getText().trim();

        if (host.isEmpty() || port.isEmpty() || dbName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            lblStatus.setText("Error: Please fill in all fields.");
            return;
        }

        // 1. Construct the JDBC URL
        // Format: jdbc:mysql://[host]:[port]/[database]?serverTimezone=UTC
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?serverTimezone=UTC";

        // 2. Save to config.properties using ConfigManager
        ConfigManager cm = new ConfigManager();
        boolean saved = cm.saveConfig(url, user, pass);

        if (!saved) {
            lblStatus.setText("Error: Could not write to config file.");
            return;
        }

        // 3. Test Connection immediately
        lblStatus.setText("Connecting...");
        // Force reload of config in DBConnectionManager
        DBConnectionManager.loadConfig();

        try (Connection conn = DBConnectionManager.getConnection()) {
            if (conn != null) {
                // SUCCESS: Navigate to Menu Scene
                navigateToMenu();
            }
        } catch (SQLException ex) {
            lblStatus.setText("Connection Failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void navigateToMenu() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new MenuScene());
    }
}