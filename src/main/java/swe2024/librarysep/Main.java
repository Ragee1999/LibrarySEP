package swe2024.librarysep;

import javafx.application.Application;
import javafx.stage.Stage;
import swe2024.librarysep.Utility.SceneManager;

public class Main extends Application {
        @Override
        public void start(Stage primaryStage) {
                SceneManager.setPrimaryStage(primaryStage);
                SceneManager.showLogin();
        }

        public static void main(String[] args) {
                launch(args);
        }
}

