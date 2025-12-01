package panes;

import dao.BookingDAO;
import model.Booking;
import scenes.MenuScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageBookingPane extends BorderPane {

    private final BookingDAO bookingDAO = new BookingDAO();

    public ManageBookingPane() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar();

        Label header = new Label("Find / Edit Order");
        header.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 18));
        header.setTextFill(AppConstants.PRIMARY_COLOR);

        TextField txtSearchId = new TextField();
        txtSearchId.setPromptText("Booking ID (e.g., B-173...)");

        Button btnSearch = new Button("Search");
        btnSearch.setBackground(new Background(AppConstants.secondaryColorRadii));
        btnSearch.setTextFill(Color.WHITE);

        TextArea infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setPrefHeight(100);

        TextField txtNewDuration = new TextField();
        txtNewDuration.setPromptText("New Days");

        Button btnUpdate = new Button("Update Duration");
        btnUpdate.setDisable(true);
        btnUpdate.setBackground(new Background(AppConstants.secondaryColorRadii));
        btnUpdate.setTextFill(Color.WHITE);

        Button btnDelete = new Button("Delete Order");
        btnDelete.setDisable(true);
        btnDelete.setStyle("-fx-text-fill: red;");

        btnSearch.setOnAction(e -> {
            Booking b = bookingDAO.findBookingById(txtSearchId.getText().trim());
            if (b != null) {
                infoArea.setText(
                        "Name: " + b.getCustomer().getName() +
                                "\nCar: " + b.getVehicleType().getType() +
                                "\nDays: " + b.getDurationDays() +
                                "\nTotal: $" + b.getTotalPrice()
                );
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                infoArea.setText("Booking not found. Please check ID.");
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnUpdate.setOnAction(e -> {
            try {
                if (bookingDAO.updateBookingDuration(
                        txtSearchId.getText(),
                        Integer.parseInt(txtNewDuration.getText())
                )) {
                    showAlert("Success", "Updated!", Alert.AlertType.INFORMATION);
                    btnSearch.fire();
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid Number", Alert.AlertType.ERROR);
            }
        });

        btnDelete.setOnAction(e -> {
            if (bookingDAO.deleteBooking(txtSearchId.getText())) {
                showAlert("Success", "Deleted!", Alert.AlertType.INFORMATION);
                infoArea.clear();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        HBox searchBox = new HBox(10, txtSearchId, btnSearch);
        HBox updateBox = new HBox(10, txtNewDuration, btnUpdate);

        root.getChildren().addAll(
                topBar,
                header,
                new Label("Enter ID:"),
                searchBox,
                new Separator(),
                infoArea,
                updateBox,
                btnDelete
        );

        setCenter(root);
        setBackground(new Background(AppConstants.backgroundColor));
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setBackground(new Background(AppConstants.primaryColorFill));

        Button btnBack = new Button(AppConstants.BTN_BACK);
        btnBack.setTextFill(Color.WHITE);
        btnBack.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.SEMI_BOLD, 14));
        btnBack.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY
        )));
        btnBack.setOnAction(e -> navigateToMenu());

        topBar.getChildren().add(btnBack);
        return topBar;
    }

    private void navigateToMenu() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new MenuScene());
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}