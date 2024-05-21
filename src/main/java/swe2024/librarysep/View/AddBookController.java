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

    public AddBookController() {}

    public void setViewModel(AddBookViewModel viewModel) {
        this.viewModel = viewModel;
    }

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to add this book?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    private void handleCancel() {
        SceneManager.showAdminDashboard();
    }
}



