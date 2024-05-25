package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.ViewModel.RegistrationViewModel;

/**
 * View for the Registration view.
 * Handles user input and interactions for user registration.
 */
public class RegistrationView {

    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField passwordRegister;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button goBackToLoginButton;

    private RegistrationViewModel viewModel;

    /**
     * Sets the ViewModel for the Registration view.
     *
     * @param viewModel the RegistrationViewModel to be set
     */
    public void setViewModel(RegistrationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Initializes the View. Binds UI components to the ViewModel properties and sets up event handlers.
     */
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
                    goBackToLogin(); // Goes back to the login screen after account creation for user-friendliness
                }
                viewModel.registrationStatusProperty().set(""); // Resets UI status message to prevent repeated messages
            }
        });
    }

    /**
     * Navigates back to the login view.
     */
    private void goBackToLogin() {
        SceneManager.showLogin();
    }

    /**
     * Handles the action when the user clicks the create account button.
     */
    private void handleCreateAccount() {
        viewModel.registerAccount();
    }

    /**
     * Shows an alert dialog for registration messages.
     *
     * @param title   the title of the alert
     * @param message the message of the alert
     */
    private void showRegistrationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

