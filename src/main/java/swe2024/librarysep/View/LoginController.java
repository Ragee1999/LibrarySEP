package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import swe2024.librarysep.ViewModel.LoginViewModel;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private LoginViewModel loginViewModel;

    public LoginController(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

    @FXML
    private void initialize() {
        // Bind text fields to ViewModel properties
        usernameField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());

        registerButton.setOnAction(event -> loginViewModel.showRegistration());
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        boolean success = loginViewModel.authenticate();
        if (!success) {
            showLoginAlert("Login Failed", "Invalid username or password");
        }
    }

    private void showLoginAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}