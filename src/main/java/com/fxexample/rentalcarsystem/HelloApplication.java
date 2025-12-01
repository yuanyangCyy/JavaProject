package com.fxexample.rentalcarsystem;

import dao.SetupDAO;
import scenes.IntroScene;
import javafx.application.Application;
import javafx.stage.Stage;
import util.DBConnectionManager;
import panes.AppConstants;

import java.io.IOException;

public class HelloApplication extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        if (!DBConnectionManager.loadConfig()) {
            System.err.println("Failed to load DB Config");
            return;
        }

        new SetupDAO().resetDatabase();

        mainStage.setTitle(AppConstants.APP_NAME);
        mainStage.setMinWidth(AppConstants.SCREEN_WIDTH);
        mainStage.setMinHeight(AppConstants.SCREEN_HEIGHT);
        mainStage.setScene(new IntroScene());
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}