package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.ViewModel.AddBookViewModel;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controller for the Add Book view. Handles user input and interactions for adding a new book.
 */
public class AddBookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField genreField;

    private AddBookViewModel viewModel;

    /**
     * Default constructor.
     */
    public AddBookController() {}

    /**
     * Sets the view model for this controller.
     *
     * @param viewModel the view model to set
     */
    public void setViewModel(AddBookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Handles the action of adding a book. Fetches user input, validates it,
     * and interacts with the view model to add the book.
     */
    @FXML
    private void handleAddBook() {
        try {
            // Fetches user input from the UI elements
            String title = titleField.getText();
            String author = authorField.getText();
            int releaseYear = Integer.parseInt(releaseYearField.getText());
            String genre = genreField.getText();

            // User confirmation
            if (showConfirmationDialog()) {
                viewModel.addBook(title, author, releaseYear, genre);  // Call the ViewModel to add the book
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");
                SceneManager.showAdminDashboard();
            }

            // Error handling with UI alerts/feedback
        } catch (NumberFormatException e) {
            // Show error alert if release year is not a valid number
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Make sure to fill out all fields and use only number values in the release year field.");
        } catch (SQLException | RemoteException e) {
            // Show error alert for SQL or Remote exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the book: " + e.getMessage());
        } catch (RuntimeException e) {
            // Show a generic error alert for any other exceptions
            showAlert(Alert.AlertType.INFORMATION, "Cancelled", e.getMessage());
        }
    }

    /**
     * Shows an alert dialog with the specified type, title, and message.
     *
     * @param alertType the type of alert
     * @param title     the title of the alert
     * @param message   the message of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog to confirm the action of adding a book.
     *
     * @return true if the user confirms, false otherwise
     */
    private boolean showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to add this book?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Handles the action of canceling the add book operation.
     * Navigates back to the admin dashboard.
     */
    @FXML
    private void handleCancel() {
        SceneManager.showAdminDashboard();
    }
}



