package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import swe2024.librarysep.Server.RMIBookServiceFactory;
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

    public AddBookController() {
        // Initialize the ViewModel with a BookService instance
        this.viewModel = new AddBookViewModel(RMIBookServiceFactory.getBookService());
    }

    @FXML
    private void handleAddBook() throws SQLException, RemoteException {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            int releaseYear = Integer.parseInt(releaseYearField.getText());
            String genre = genreField.getText();

            showConfirmationDialog();
            viewModel.addBook(title, author, releaseYear, genre);  // Call the ViewModel to add the book

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");
            SceneManager.showAdminDashboard();
        } catch (NumberFormatException e) {
            // Show error alert if release year is not a valid number
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Make sure to fill out all fields and only number values in the release year field.");
        } catch (Exception e) {
            // Show a generic error alert for any other exceptions
            showAlert(Alert.AlertType.INFORMATION, "Cancel", e.getMessage());
            // Since the only type of errors that can happen is not typing a number in the release year, and the 3 other fields are strings
            // Which means you can't really produce an error out of those other fields.
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to add this book?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        } else {
            throw new RuntimeException("Operation cancelled by user."); // This alert is for the cancel operation
        }
    }

    @FXML
    private void handleCancel() {
        SceneManager.showAdminDashboard();
    }
}



