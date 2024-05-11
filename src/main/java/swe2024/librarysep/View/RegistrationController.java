package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import swe2024.librarysep.Main;
import swe2024.librarysep.ViewModel.RegistrationViewModel;

public class RegistrationController {

    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField passwordRegister;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button goBackToLoginButton;

    private RegistrationViewModel viewModel = new RegistrationViewModel();

    @FXML
    private void initialize() {
        // Property-binding with the RegistrationViewModel
        usernameRegister.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordRegister.textProperty().bindBidirectional(viewModel.passwordProperty());

        // Initializing clickable buttons
        createAccountButton.setOnAction(event -> handleCreateAccount());
        goBackToLoginButton.setOnAction(event -> goBackToLogin());

        // Listener that checks for either Success or Error and sends messages accordingly
        viewModel.registrationStatusProperty().addListener((observableValue, oldStatus, newStatus) -> {
            if (newStatus != null && !newStatus.isEmpty()) {
                showRegistrationAlert(newStatus.startsWith("Success") ? "Success" : "Error", newStatus);
                if (newStatus.startsWith("Success")) {
                    goBackToLogin(); // Goes back to the login screen after account creation for user friendly-ness
                }
                viewModel.registrationStatusProperty().set(""); // Resets UI status message to prevent repeated messages
            }
        });
    }

    private void goBackToLogin() {
        Main.showLogin();
    }

    private void handleCreateAccount() {
        viewModel.registerAccount();
    }

    private void showRegistrationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
