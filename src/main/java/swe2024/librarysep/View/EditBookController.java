package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.ViewModel.EditBookViewModel;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class EditBookController {
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField releaseYearField;
    @FXML private TextField genreField;

    private EditBookViewModel viewModel;

    public EditBookController() {}

    public void setViewModel(EditBookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    private void initialize() {
        if (viewModel != null) {
            bindFields();
        }
    }

    public void setBook(Book book) {
        viewModel.setBook(book);
        bindFields();
    }

    private void bindFields() {
        titleField.textProperty().bindBidirectional(viewModel.titleProperty());
        authorField.textProperty().bindBidirectional(viewModel.authorProperty());
        releaseYearField.textProperty().bindBidirectional(viewModel.releaseYearProperty());
        genreField.textProperty().bindBidirectional(viewModel.genreProperty());
    }


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

    @FXML
    private void handleCancel() {
        SceneManager.showAdminDashboard();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
