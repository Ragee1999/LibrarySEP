package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import swe2024.librarysep.Main;
import swe2024.librarysep.Model.SessionManager;
import swe2024.librarysep.Model.User;
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

    private LoginViewModel loginViewModel = new LoginViewModel();

    @FXML
    private void initialize() {
        // Bind text fields to ViewModel properties
        usernameField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());

        registerButton.setOnAction(event -> Main.showRegistration());
        loginButton.setOnAction(event -> handleLogin());
    }


    private void handleLogin() {
        User user = loginViewModel.authenticateUser();
        if (user != null) {
            SessionManager.loginUser(user); // Log the user into the session
            System.out.println("User Successfully logged in");
            Main.showDashboard(); // Navigate to the dashboard
            System.out.println("User authenticated with Database");
        } else {
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
