package view;

import dao.BookingDAO;
import dao.VehicleDAO;
import model.Booking;
import model.VehicleType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.time.LocalDate;

/**
 * ViewFactory (Updated)
 * - Increased Image Size
 * - Displays Booking ID on Success
 * - Detailed Admin View
 */
public class ViewFactory {

    private final Stage stage;
    private final BookingDAO bookingDAO = new BookingDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    public ViewFactory(Stage stage) {
        this.stage = stage;
    }

    // =========================================================
    // 1. HOME SCREEN
    // =========================================================
    public void showHomeScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Rental System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button btnBook = new Button("1. Book a Car");
        Button btnManage = new Button("2. Manage Orders");
        Button btnAdmin = new Button("3. Admin View");

        String btnStyle = "-fx-font-size: 14px; -fx-min-width: 200px; -fx-min-height: 40px;";
        btnBook.setStyle(btnStyle);
        btnManage.setStyle(btnStyle);
        btnAdmin.setStyle(btnStyle);

        btnBook.setOnAction(e -> showBookingScene());
        btnManage.setOnAction(e -> showManageScene());
        btnAdmin.setOnAction(e -> showAdminScene());

        root.getChildren().addAll(title, new Separator(), btnBook, btnManage, btnAdmin);
        stage.setScene(new Scene(root, 400, 450));
        stage.setTitle("Home");
    }

    // =========================================================
    // 2. BOOKING SCREEN
    // =========================================================
    public void showBookingScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label header = new Label("New Booking");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // --- Vehicle Selection ---
        HBox carSelectionBox = new HBox(20);
        carSelectionBox.setAlignment(Pos.TOP_LEFT);

        VBox selectionLeft = new VBox(10);

        // 1. Dropdown (Sorted by Price via DAO)
        ComboBox<VehicleType> comboVehicles = new ComboBox<>();
        comboVehicles.setItems(FXCollections.observableArrayList(vehicleDAO.getAllVehicles()));
        comboVehicles.setPromptText("Select a Vehicle...");
        comboVehicles.setPrefWidth(200);

        Label lblRate = new Label("Daily Rate: $0.00");
        lblRate.setStyle("-fx-font-weight: bold;");
        selectionLeft.getChildren().addAll(new Label("Car Type:"), comboVehicles, lblRate);

        // 2. Image (Size Increased ~50%)
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);  // Increased from 200
        imageView.setFitHeight(180); // Increased from 120
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        Label lblImgStatus = new Label("No Car Selected");
        VBox imageContainer = new VBox(5, imageView, lblImgStatus);
        imageContainer.setAlignment(Pos.CENTER);

        carSelectionBox.getChildren().addAll(selectionLeft, imageContainer);

        // --- Date & Logic ---
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<Integer> comboDays = new ComboBox<>();
        for (int i = 1; i <= 30; i++) comboDays.getItems().add(i);
        comboDays.getSelectionModel().selectFirst();

        Label lblTotal = new Label("Total Price: $0.00");
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblTotal.setTextFill(Color.BLUE);

        // Dynamic Update
        Runnable updateUI = () -> {
            VehicleType v = comboVehicles.getValue();
            Integer days = comboDays.getValue();
            if (v != null) {
                double rate = v.getDailyRate();
                lblRate.setText("Daily Rate: $" + rate);
                if (days != null) lblTotal.setText(String.format("Total Price: $%.2f", rate * days));

                try {
                    Image img = new Image(v.getImagePath(), true);
                    imageView.setImage(img);
                    lblImgStatus.setText("");
                } catch (Exception ex) { lblImgStatus.setText("Image Error"); }
            }
        };
        comboVehicles.setOnAction(e -> updateUI.run());
        comboDays.setOnAction(e -> updateUI.run());

        // --- User Info ---
        TextField txtName = new TextField(); txtName.setPromptText("Full Name");
        TextField txtPhone = new TextField(); txtPhone.setPromptText("Phone Number");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email");

        Button btnSubmit = new Button("Submit Booking");
        btnSubmit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnBack = new Button("< Back");
        btnBack.setOnAction(e -> showHomeScene());

        // --- Submit Logic (Now shows Booking ID) ---
        btnSubmit.setOnAction(e -> {
            try {
                if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Please fill all fields.");
                    return;
                }
                if (comboVehicles.getValue() == null) {
                    showAlert("Validation Error", "Select a vehicle.");
                    return;
                }

                // Call DAO and get ID
                String newBookingId = bookingDAO.createBooking(
                        txtName.getText(), txtPhone.getText(), txtEmail.getText(),
                        comboVehicles.getValue(), datePicker.getValue(), comboDays.getValue()
                );

                if (newBookingId != null) {
                    // Show the ID to the user!
                    showBookingSuccessDialog(newBookingId);
                    showHomeScene();
                } else {
                    showAlert("Error", "Database Error.");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        root.getChildren().addAll(btnBack, header, new Separator(), carSelectionBox,
                new Label("Start Date:"), datePicker, new Label("Duration (Days):"), comboDays, lblTotal,
                new Separator(), new Label("Customer Info:"), txtName, txtPhone, txtEmail, new Separator(), btnSubmit);

        stage.setScene(new Scene(root, 600, 750)); // Increased Window Width for larger image
    }

    // =========================================================
    // 3. MANAGE ORDER
    // =========================================================
    public void showManageScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label header = new Label("Find / Edit Order");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField txtSearchId = new TextField(); txtSearchId.setPromptText("Booking ID (e.g., B-173...)");
        Button btnSearch = new Button("Search");
        TextArea infoArea = new TextArea(); infoArea.setEditable(false); infoArea.setPrefHeight(100);
        TextField txtNewDuration = new TextField(); txtNewDuration.setPromptText("New Days");
        Button btnUpdate = new Button("Update Duration");
        Button btnDelete = new Button("Delete Order");
        btnUpdate.setDisable(true); btnDelete.setDisable(true); btnDelete.setStyle("-fx-text-fill: red;");

        btnSearch.setOnAction(e -> {
            Booking b = bookingDAO.findBookingById(txtSearchId.getText().trim());
            if(b!=null) {
                infoArea.setText("Name: " + b.getCustomer().getName() + "\nCar: " + b.getVehicleType().getType() +
                        "\nDays: " + b.getDurationDays() + "\nTotal: $" + b.getTotalPrice());
                btnUpdate.setDisable(false); btnDelete.setDisable(false);
            } else {
                infoArea.setText("Booking not found. Please check ID.");
                btnUpdate.setDisable(true); btnDelete.setDisable(true);
            }
        });

        btnUpdate.setOnAction(e -> {
            try {
                if(bookingDAO.updateBookingDuration(txtSearchId.getText(), Integer.parseInt(txtNewDuration.getText()))) {
                    showAlert("Success", "Updated!");
                    btnSearch.fire();
                }
            } catch(Exception ex) { showAlert("Error", "Invalid Number"); }
        });

        btnDelete.setOnAction(e -> {
            if(bookingDAO.deleteBooking(txtSearchId.getText())) {
                showAlert("Success", "Deleted!");
                infoArea.clear();
                btnUpdate.setDisable(true); btnDelete.setDisable(true);
            }
        });

        Button btnBack = new Button("< Back"); btnBack.setOnAction(e->showHomeScene());
        root.getChildren().addAll(btnBack, header, new Label("Enter ID:"), new HBox(10, txtSearchId, btnSearch),
                new Separator(), infoArea, new HBox(10, txtNewDuration, btnUpdate), btnDelete);
        stage.setScene(new Scene(root, 400, 500));
    }

    // =========================================================
    // 4. ADMIN SCREEN (Detailed)
    // =========================================================
    public void showAdminScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label header = new Label("Admin Dashboard");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Booking> table = new TableView<>();

        // ID
        TableColumn<Booking, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colId.setPrefWidth(120);

        // Name
        TableColumn<Booking, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getName()));

        // Phone
        TableColumn<Booking, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getPhone()));

        // Car
        TableColumn<Booking, String> colCar = new TableColumn<>("Car");
        colCar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVehicleType().getType()));

        // Days
        TableColumn<Booking, Integer> colDays = new TableColumn<>("Days");
        colDays.setCellValueFactory(new PropertyValueFactory<>("durationDays"));

        // Price
        TableColumn<Booking, Double> colPrice = new TableColumn<>("Total ($)");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        table.getColumns().addAll(colId, colName, colPhone, colCar, colDays, colPrice);
        table.setItems(FXCollections.observableArrayList(bookingDAO.getAllBookings()));

        Button btnBack = new Button("< Back");
        btnBack.setOnAction(e -> showHomeScene());

        Button btnRefresh = new Button("Refresh");
        btnRefresh.setOnAction(e -> table.setItems(FXCollections.observableArrayList(bookingDAO.getAllBookings())));

        root.getChildren().addAll(btnBack, header, table, btnRefresh);
        stage.setScene(new Scene(root, 750, 500)); // Wider window for more columns
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Default to Error/Warning style
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Helper for Success messages (Information Icon)
    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Custom dialog that allows the user to copy the Booking ID easily.
     */
    private void showBookingSuccessDialog(String bookingId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Successful");
        alert.setHeaderText("Booking Saved Successfully!");

        // Create a layout for the custom content
        VBox content = new VBox(10);

        Label lbl = new Label("Please save your Booking ID:");

        // TextField makes the ID selectable/copyable
        TextField txtId = new TextField(bookingId);
        txtId.setEditable(false); // Read-only
        txtId.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // "Copy" Button
        Button btnCopy = new Button("Copy ID to Clipboard");
        btnCopy.setOnAction(e -> {
            ClipboardContent cc = new ClipboardContent();
            cc.putString(bookingId);
            Clipboard.getSystemClipboard().setContent(cc);
            btnCopy.setText("Copied!"); // Visual feedback
        });

        content.getChildren().addAll(lbl, txtId, btnCopy);

        // Set this custom layout into the Alert
        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }
}