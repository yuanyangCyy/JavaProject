package com.fxexample.rentalcarsystem;

import dao.SetupDAO;
import view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import util.DBConnectionManager;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Load Config
        if (!DBConnectionManager.loadConfig()) {
            System.err.println("Failed to load DB Config");
        }

        // 2. OPTIONAL: Reset Database on startup?
        // Uncomment this line if you want to wipe data and inject new images every time you run.
        new SetupDAO().resetDatabase();

        // 3. Initialize View
        ViewFactory viewFactory = new ViewFactory(stage);
        viewFactory.showHomeScene();
        stage.show();
    }

    public static void main(String[] args) {
//         Create an instance of SetupDAO to reset data manually if needed before launch
//         System.out.println("Resetting Database...");
        new SetupDAO().resetDatabase();

        launch();
    }
}