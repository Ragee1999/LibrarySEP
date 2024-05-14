package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.ViewModel.userDashboardViewModel;
import static swe2024.librarysep.Model.SessionManager.getCurrentUser;

//
// This class is almost similar to DashboardController, the only difference being constructor having 2 fewer columns
//

public class userDashboardController {
    @FXML
    private TableView<Book> bookTableViewUser;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> releaseYearColumn;
    @FXML
    private TableColumn<Book, String> stateColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TextField userSearchTextField;
    @FXML
    private MenuButton userFilterDropdownMenu;

    // Declaration of DashboardViewModel Instance
    private userDashboardViewModel viewModelUser;

    public void setViewModel(userDashboardViewModel viewModelUser) {
        this.viewModelUser = viewModelUser;
        // bookTableViewUser.setItems(viewModelUser.getBooks());
        bookTableViewUser.setItems(viewModelUser.getFilteredBooks());
        viewModelUser.bindTableColumns(titleColumn, authorColumn, releaseYearColumn, stateColumn, genreColumn);

        // Bind search text field to update the filter predicate
        userSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            viewModelUser.setUserSearchQuery(newValue);
        });

        // Binds the error message property to show alerts on changes
        this.viewModelUser.errorMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showStateAlert(newValue);
                this.viewModelUser.errorMessageProperty().set(""); // Also resets the message to prevent repeated alerts
            }
        });

        // Add genre filter items to the dropdown menu
        for (String genre : viewModelUser.getGenres()) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(event -> viewModelUser.setGenreFilter(genre));
            userFilterDropdownMenu.getItems().add(item);
        }

        // Add a "Clear Filter" item
        MenuItem clearFilter = new MenuItem("Clear Filter");
        clearFilter.setOnAction(event -> viewModelUser.setGenreFilter(null));
        userFilterDropdownMenu.getItems().add(clearFilter);
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.borrowBook(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.returnBook(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }

    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.reserveBook(selectedBook, currentUser);
        } else {
            System.out.println("No book selected");
        }
    }

    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.cancelReservation(selectedBook, currentUser);
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
}
