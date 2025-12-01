package panes;

import dao.BookingDAO;
import dao.VehicleDAO;
import model.VehicleType;
import scenes.MenuScene;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

public class BookingPane extends BorderPane {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    public BookingPane() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar();

        Label header = new Label("New Booking");
        header.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 18));
        header.setTextFill(AppConstants.PRIMARY_COLOR);

        HBox carSelectionBox = new HBox(20);
        carSelectionBox.setAlignment(Pos.TOP_LEFT);

        VBox selectionLeft = new VBox(10);

        ComboBox<VehicleType> comboVehicles = new ComboBox<>();
        comboVehicles.setItems(FXCollections.observableArrayList(vehicleDAO.getAllVehicles()));
        comboVehicles.setPromptText("Select a Vehicle...");
        comboVehicles.setPrefWidth(200);

        Label lblRate = new Label("Daily Rate: $0.00");
        lblRate.setStyle("-fx-font-weight: bold;");
        selectionLeft.getChildren().addAll(new Label("Car Type:"), comboVehicles, lblRate);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        Label lblImgStatus = new Label("No Car Selected");
        VBox imageContainer = new VBox(5, imageView, lblImgStatus);
        imageContainer.setAlignment(Pos.CENTER);

        carSelectionBox.getChildren().addAll(selectionLeft, imageContainer);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<Integer> comboDays = new ComboBox<>();
        for (int i = 1; i <= 30; i++) comboDays.getItems().add(i);
        comboDays.getSelectionModel().selectFirst();

        Label lblTotal = new Label("Total Price: $0.00");
        lblTotal.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 14));
        lblTotal.setTextFill(AppConstants.SECONDARY_COLOR);

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
                } catch (Exception ex) {
                    lblImgStatus.setText("Image Error");
                }
            }
        };
        comboVehicles.setOnAction(e -> updateUI.run());
        comboDays.setOnAction(e -> updateUI.run());

        TextField txtName = new TextField();
        txtName.setPromptText("Full Name");
        TextField txtPhone = new TextField();
        txtPhone.setPromptText("Phone Number");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        Button btnSubmit = new Button("Submit Booking");
        btnSubmit.setBackground(new Background(AppConstants.secondaryColorRadii));
        btnSubmit.setTextFill(Color.WHITE);
        btnSubmit.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 14));

        btnSubmit.setOnAction(e -> {
            try {
                if (txtName.getText().trim().isEmpty() ||
                        txtPhone.getText().trim().isEmpty() ||
                        txtEmail.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Please fill all fields.");
                    return;
                }
                if (comboVehicles.getValue() == null) {
                    showAlert("Validation Error", "Select a vehicle.");
                    return;
                }

                String newBookingId = bookingDAO.createBooking(
                        txtName.getText(),
                        txtPhone.getText(),
                        txtEmail.getText(),
                        comboVehicles.getValue(),
                        datePicker.getValue(),
                        comboDays.getValue()
                );

                if (newBookingId != null) {
                    showBookingSuccessDialog(newBookingId);
                    navigateToMenu();
                } else {
                    showAlert("Error", "Database Error.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(
                topBar,
                header,
                new Separator(),
                carSelectionBox,
                new Label("Start Date:"),
                datePicker,
                new Label("Duration (Days):"),
                comboDays,
                lblTotal,
                new Separator(),
                new Label("Customer Info:"),
                txtName,
                txtPhone,
                txtEmail,
                new Separator(),
                btnSubmit
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showBookingSuccessDialog(String bookingId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Successful");
        alert.setHeaderText("Booking Saved Successfully!");

        VBox content = new VBox(10);

        Label lbl = new Label("Please save your Booking ID:");

        TextField txtId = new TextField(bookingId);
        txtId.setEditable(false);
        txtId.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnCopy = new Button("Copy ID to Clipboard");
        btnCopy.setOnAction(e -> {
            ClipboardContent cc = new ClipboardContent();
            cc.putString(bookingId);
            Clipboard.getSystemClipboard().setContent(cc);
            btnCopy.setText("Copied!");
        });

        content.getChildren().addAll(lbl, txtId, btnCopy);
        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }
}