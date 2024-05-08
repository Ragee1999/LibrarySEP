package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DashboardController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}