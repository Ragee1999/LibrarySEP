package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.ViewModel.EditBookViewModel;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Controller for the Edit Book view. Handles user input and interactions for editing a book.
 */
public class EditBookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField genreField;

    private EditBookViewModel viewModel;

    /**
     * Default constructor.
     */
    public EditBookController() {}

    /**
     * Sets the view model for this controller.
     *
     * @param viewModel the view model to set
     */
    public void setViewModel(EditBookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Initializes the controller. Binds the UI fields to the view model properties if the view model is set.
     */
    @FXML
    private void initialize() {
        if (viewModel != null) {
            bindFields();
        }
    }

    /**
     * Sets the book to be edited and binds the UI fields to the view model properties.
     *
     * @param book the book to be edited
     */
    public void setBook(Book book) {
        viewModel.setBook(book);
        bindFields();
    }

    /**
     * Binds the UI fields to the view model properties for bidirectional data binding.
     */
    private void bindFields() {
        titleField.textProperty().bindBidirectional(viewModel.titleProperty());
        authorField.textProperty().bindBidirectional(viewModel.authorProperty());
        releaseYearField.textProperty().bindBidirectional(viewModel.releaseYearProperty());
        genreField.textProperty().bindBidirectional(viewModel.genreProperty());
    }

    /**
     * Handles the action of saving the edited book. Validates input and updates the book through the view model.
     */
    @FXML
    private void handleBookSave() {
        try {
            viewModel.editBook(); // ViewModel handles updating the book and input validation
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book updated successfully!");
            SceneManager.showAdminDashboard();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid input for Release Year: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", e.getMessage());
        } catch (RemoteException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the book: " + e.getMessage());
        }
    }

    /**
     * Handles the action of canceling the edit operation. Navigates back to the admin dashboard.
     */
    @FXML
    private void handleCancel() {
        SceneManager.showAdminDashboard();
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
}

