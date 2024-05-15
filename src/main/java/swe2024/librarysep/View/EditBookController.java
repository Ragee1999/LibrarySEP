package swe2024.librarysep.View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import swe2024.librarysep.Main;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Server.RMIBookServiceFactory;
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

    public EditBookController() {
        this.viewModel = new EditBookViewModel(RMIBookServiceFactory.getBookService());
    }

    public void initialize() {
        titleField.textProperty().bindBidirectional(viewModel.titleProperty());
        authorField.textProperty().bindBidirectional(viewModel.authorProperty());
        releaseYearField.textProperty().bindBidirectional(viewModel.releaseYearProperty());
        genreField.textProperty().bindBidirectional(viewModel.genreProperty());
    }

    public void setBook(Book book) {
        viewModel.setBook(book);
    }
    @FXML
    private void handleBookSave() {
        try {
            viewModel.updateBook(); // ViewModel handles updating the book
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book updated successfully!");
            alert.showAndWait();
            SceneManager.showAdminDashboard();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input for Release Year!");
            alert.showAndWait();
        } catch (RemoteException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update the book: " + e.getMessage());
            alert.showAndWait();
        }
    }
}