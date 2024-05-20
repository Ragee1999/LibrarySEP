package swe2024.librarysep;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import swe2024.librarysep.Database.DatabaseConnection;
import swe2024.librarysep.Utility.SceneManager;

public class Main extends Application {


        @Override
        public void start(Stage primaryStage) {
                SceneManager.setPrimaryStage(primaryStage);
                SceneManager.showLogin();

                // Properly closes the application when exiting the application
                // Ensure the application exits when the primary stage is closed
                primaryStage.setOnCloseRequest(event -> {
                        Platform.exit();
                        System.exit(0);
                });
        }

        public static void main(String[] args) {
                // Register a shutdown hook to close all connections when the application exits
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        DatabaseConnection.closeAllConnections();
                        System.out.println("All connections closed.");
                }));
                // Launch the JavaFX application
                launch(args);
        }
}

