package panes;

import scenes.BookingScene;
import scenes.ManageBookingScene;
import scenes.AdminScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MenuPane extends BorderPane {

    public MenuPane() {
        VBox mainLayout = new VBox(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));

        Label header = new Label(AppConstants.APP_NAME);
        header.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 36));
        header.setTextFill(AppConstants.PRIMARY_COLOR);

        Label subtitle = new Label("Choose an option to continue");
        subtitle.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.NORMAL, 18));
        subtitle.setTextFill(AppConstants.GRAY_COLOR);

        Button btnBook = createMenuButton(AppConstants.BTN_BOOK, "Book your rental vehicle");
        Button btnManage = createMenuButton(AppConstants.BTN_MANAGE, "View or modify your bookings");
        Button btnAdmin = createMenuButton(AppConstants.BTN_ADMIN, "View all bookings and analytics");

        btnBook.setOnAction(e -> navigateTo(new BookingScene()));
        btnManage.setOnAction(e -> navigateTo(new ManageBookingScene()));
        btnAdmin.setOnAction(e -> navigateTo(new AdminScene()));

        VBox buttonContainer = new VBox(20, btnBook, btnManage, btnAdmin);
        buttonContainer.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(header, subtitle, buttonContainer);

        setCenter(mainLayout);
        setBackground(new Background(new BackgroundFill(
                Color.rgb(250, 250, 250), CornerRadii.EMPTY, Insets.EMPTY
        )));
    }

    private Button createMenuButton(String text, String description) {
        VBox buttonContent = new VBox(5);

        Label titleLabel = new Label(text);
        titleLabel.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.SEMI_BOLD, 16));
        titleLabel.setTextFill(AppConstants.PRIMARY_COLOR);

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.NORMAL, 12));
        descLabel.setTextFill(AppConstants.GRAY_COLOR);
        descLabel.setWrapText(true);

        buttonContent.getChildren().addAll(titleLabel, descLabel);
        buttonContent.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setPrefWidth(400);
        button.setPrefHeight(80);
        button.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-border-color: " + toRgbString(AppConstants.PRIMARY_COLOR) + "; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 15px;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: " + toRgbString(AppConstants.TERTIARY_COLOR) + "; " +
                        "-fx-border-color: " + toRgbString(AppConstants.SECONDARY_COLOR) + "; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 15px;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-border-color: " + toRgbString(AppConstants.PRIMARY_COLOR) + "; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 15px;"
        ));

        return button;
    }

    private void navigateTo(Scene scene) {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(scene);
    }

    private String toRgbString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }
}