package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import swe2024.librarysep.ViewModel.LoginViewModel;

public class LoginController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    private final LoginViewModel viewModel = new LoginViewModel();

    @FXML
    private void initialize() {
        usernameTextField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordTextField.textProperty().bindBidirectional(viewModel.passwordProperty());
    }

    @FXML
    private void handleLogin() {
        if (viewModel.attemptLogin()) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }
    }
}