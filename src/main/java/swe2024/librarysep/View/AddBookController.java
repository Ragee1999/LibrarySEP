package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import swe2024.librarysep.Main;
import swe2024.librarysep.Server.RMIBookServiceFactory;
import swe2024.librarysep.ViewModel.AddBookViewModel;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class AddBookController {
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField releaseYearField;
    @FXML private TextField genreField;

    private AddBookViewModel viewModel;

    public AddBookController() {
        // Initialize the ViewModel with a BookService instance
        this.viewModel = new AddBookViewModel(RMIBookServiceFactory.getBookService());
    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Add Book Confirmation");
        alert.setContentText("The book has been added successfully");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Main.showAdminDashboard();
            }
        });
    }

    @FXML
    private void handleAddBook() throws SQLException, RemoteException {
        String title = titleField.getText();
        String author = authorField.getText();
        int releaseYear = Integer.parseInt(releaseYearField.getText());
        String genre = genreField.getText();
        showConfirmationDialog();
        viewModel.addBook(title, author, releaseYear, genre);
    }
}



