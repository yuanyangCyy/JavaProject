package panes;

import dao.BookingDAO;
import model.Booking;
import scenes.MenuScene;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminPane extends BorderPane {

    private final BookingDAO bookingDAO = new BookingDAO();

    public AdminPane() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar();

        Label header = new Label("Admin Dashboard");
        header.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 18));
        header.setTextFill(AppConstants.PRIMARY_COLOR);

        TableView<Booking> table = new TableView<>();

        TableColumn<Booking, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colId.setPrefWidth(120);

        TableColumn<Booking, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCustomer().getName())
        );

        TableColumn<Booking, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCustomer().getPhone())
        );

        TableColumn<Booking, String> colCar = new TableColumn<>("Car");
        colCar.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getVehicleType().getType())
        );

        TableColumn<Booking, Integer> colDays = new TableColumn<>("Days");
        colDays.setCellValueFactory(new PropertyValueFactory<>("durationDays"));

        TableColumn<Booking, Double> colPrice = new TableColumn<>("Total ($)");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        table.getColumns().addAll(colId, colName, colPhone, colCar, colDays, colPrice);
        table.setItems(FXCollections.observableArrayList(bookingDAO.getAllBookings()));

        Button btnRefresh = new Button("Refresh");
        btnRefresh.setBackground(new Background(AppConstants.secondaryColorRadii));
        btnRefresh.setTextFill(Color.WHITE);
        btnRefresh.setOnAction(e ->
                table.setItems(FXCollections.observableArrayList(bookingDAO.getAllBookings()))
        );

        root.getChildren().addAll(topBar, header, table, btnRefresh);

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
}