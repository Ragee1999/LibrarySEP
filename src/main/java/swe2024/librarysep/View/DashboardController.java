package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import swe2024.librarysep.Main;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.ViewModel.DashboardViewModel;

import static swe2024.librarysep.Model.SessionManager.getCurrentUser;

public class DashboardController {


    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> releaseYearColumn;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> stateColumn;
    @FXML
    private TableColumn<Book, String> clientColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;


    // Declaration of DashboardViewModel Instance
    private DashboardViewModel viewModel;


    public void setViewModel(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
        bookTableView.setItems(viewModel.getBooks());
        viewModel.bindTableColumns(titleColumn, authorColumn, releaseYearColumn, idColumn, stateColumn, clientColumn, genreColumn);

        // Binds the error message property to show alerts on changes
        this.viewModel.errorMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showStateAlert(newValue);
                this.viewModel.errorMessageProperty().set(""); // Also resets the message to prevent repeated alerts
            }
        });
    }

    @FXML
    private void handleOnClickAddBook() {
        Main.ShowAddBook();
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.borrowBook(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.returnBook(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }

    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.reserveBook(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }

    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.cancelReservation(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }


    private void showStateAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this book?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        viewModel.deleteBook(selectedBook);
                        System.out.println("Successfully deleted book");
                    } catch (RuntimeException e) {
                        showStateAlert("Failed to delete book: " + e.getCause().getMessage());
                    }
                }
            });
        } else {
            showStateAlert("No book selected.");
        }
    }
}
