package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import swe2024.librarysep.Utility.SessionManager;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SceneManager;
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

        registerButton.setOnAction(event -> SceneManager.showRegistration());
        loginButton.setOnAction(event -> handleLogin());
    }


    private void handleLogin() {
        User user = loginViewModel.authenticate();
        if (user != null) {
            if (user.getUsername().equals("Admin")) { // Check if the username is 'Admin'
                SessionManager.loginUser(user);
                System.out.println("Admin Successfully logged in");
                SceneManager.showAdminDashboard(); // Navigate to the admin dashboard
            } else {
                SessionManager.loginUser(user);
                System.out.println("User Successfully logged in");
                SceneManager.showUserDashboard(); // Navigate to the user dashboard
            }
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
