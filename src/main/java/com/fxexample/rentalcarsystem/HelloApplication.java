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

        // 1. Load Database Configuration
        if (!DBConnectionManager.loadConfig()) {
            System.err.println("Failed to load DB Config");
            return;
        }

        // 2. Initialize Database
        // We create an instance of SetupDAO to handle database operations
        SetupDAO setup = new SetupDAO();

        // [IMPORTANT] Create tables first!
        // This ensures the app works even if the database is completely empty.
        setup.createTables();

        // Then reset data (Clear old orders, inject default vehicles)
        setup.resetDatabase();

        // 3. Setup UI and Show Scene
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