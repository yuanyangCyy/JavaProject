package panes;

import scenes.MenuScene;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IntroPane extends BorderPane {

    public IntroPane() {
        VBox mainLayout = new VBox(25);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
        )));

        Label title = new Label(AppConstants.APP_NAME);
        title.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, FontPosture.REGULAR, 32));
        title.setTextFill(AppConstants.PRIMARY_COLOR);

        Label subtitle = new Label("Your trusted car rental service in " + AppConstants.BRANCH_LOCATION);
        subtitle.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 16));
        subtitle.setTextFill(AppConstants.GRAY_COLOR);
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(400);

        Label description = new Label("Book your perfect vehicle today with our easy and convenient rental App");
        description.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 14));
        description.setTextFill(AppConstants.GRAY_COLOR);
        description.setWrapText(true);
        description.setMaxWidth(400);

        Button continueBtn = new Button("Get Started");
        continueBtn.setPrefWidth(150);
        continueBtn.setPrefHeight(45);
        continueBtn.setBackground(new Background(AppConstants.secondaryColorRadii));
        continueBtn.setTextFill(Color.WHITE);
        continueBtn.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 16));

        continueBtn.setOnMouseClicked(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(new MenuScene());
        });

        VBox sidePanel = createSidePanel();

        mainLayout.getChildren().addAll(title, subtitle, description, continueBtn);

        applyAnimations(title, subtitle, description, continueBtn);

        setLeft(sidePanel);
        setCenter(mainLayout);
        BorderPane.setAlignment(sidePanel, Pos.CENTER);
        BorderPane.setAlignment(mainLayout, Pos.CENTER);
    }

    private VBox createSidePanel() {
        VBox sidePanel = new VBox(20);
        sidePanel.setPrefWidth(350);
        sidePanel.setAlignment(Pos.CENTER);
        sidePanel.setBackground(new Background(AppConstants.primaryColorFill));
        sidePanel.setPadding(new Insets(40));

        Label featuresTitle = new Label("Why Choose Us?");
        featuresTitle.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.BOLD, 24));
        featuresTitle.setTextFill(Color.WHITE);

        String[] features = {
                "Easy online booking process",
                "Competitive daily rates",
                "Wide selection of vehicles",
                "Flexible rental periods (1-30 days)",
                "Manage bookings anytime"
        };

        VBox featuresList = new VBox(15);
        for (String feature : features) {
            Label featureLabel = new Label("• " + feature);
            featureLabel.setFont(Font.font(AppConstants.getFontFamily(), FontWeight.NORMAL, 14));
            featureLabel.setTextFill(Color.WHITE);
            featureLabel.setWrapText(true);
            featuresList.getChildren().add(featureLabel);
        }

        sidePanel.getChildren().addAll(featuresTitle, featuresList);
        return sidePanel;
    }

    private void applyAnimations(Label title, Label subtitle, Label description, Button button) {
        FadeTransition titleFade = new FadeTransition(Duration.seconds(1.5), title);
        titleFade.setFromValue(0.0);
        titleFade.setToValue(1.0);

        FadeTransition subtitleFade = new FadeTransition(Duration.seconds(2), subtitle);
        subtitleFade.setFromValue(0.0);
        subtitleFade.setToValue(1.0);

        FadeTransition descFade = new FadeTransition(Duration.seconds(2.5), description);
        descFade.setFromValue(0.0);
        descFade.setToValue(1.0);

        FadeTransition buttonFade = new FadeTransition(Duration.seconds(3), button);
        buttonFade.setFromValue(0.0);
        buttonFade.setToValue(1.0);

        SequentialTransition sequence = new SequentialTransition(
                titleFade, subtitleFade, descFade, buttonFade
        );
        sequence.play();
    }
}