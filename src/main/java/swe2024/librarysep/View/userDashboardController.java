package swe2024.librarysep.View;

import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.Utility.SessionManager;
import swe2024.librarysep.ViewModel.userDashboardViewModel;

//
// This class is almost similar to AdminDashboardController, the only main difference being constructor,
// having 2 fewer columns, as well as no access to Admin features
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

    private MenuItem currentSelectedItem;

    public void setViewModel(userDashboardViewModel viewModelUser) {
        this.viewModelUser = viewModelUser;
        bookTableViewUser.setItems(viewModelUser.getBooks());
        viewModelUser.bindTableColumns(titleColumn, authorColumn, releaseYearColumn, stateColumn, genreColumn);

        // Bind search text field to update the filter predicate
        userSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            viewModelUser.setUserSearchQuery(newValue);
            updateTableViewItems();
        });

        // Binds the error message property to show alerts on changes
        this.viewModelUser.errorMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showAlert(newValue, Alert.AlertType.ERROR);
                this.viewModelUser.errorMessageProperty().set(""); // Also resets the message to prevent repeated alerts
            }
        });

        // Binds the success message property to show alerts on changes
        this.viewModelUser.successMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showAlert(newValue, Alert.AlertType.INFORMATION);
                this.viewModelUser.successMessageProperty().set(""); // Also resets the message to prevent repeated alerts
            }
        });

        // Add genre filter items to the dropdown menu
        for (String genre : viewModelUser.getGenres()) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(event -> {
                viewModelUser.setGenreFilter(genre);
                highlightSelectedItem(item);
                updateTableViewItems();
            });
            userFilterDropdownMenu.getItems().add(item);

        }

        // Add a "Clear Filter" item
        MenuItem clearFilter = new MenuItem("Clear Filter");
        clearFilter.setOnAction(event -> {
                    viewModelUser.setGenreFilter(null);
                    highlightSelectedItem(clearFilter);
                    updateTableViewItems();
                }
        );
        userFilterDropdownMenu.getItems().add(clearFilter);

        highlightSelectedItem(clearFilter);
    }

    private void highlightSelectedItem(MenuItem selectedItem) {
        if (currentSelectedItem != null) {
            currentSelectedItem.setStyle(""); // Remove previous style
        }
        selectedItem.setStyle("-fx-background-color: #d3d3d3; -fx-text-fill: black; -fx-percent-width: 100%; -fx-percent-height: 100%;");
        currentSelectedItem = selectedItem;

        // Update MenuButton text
        if ("Clear Filter".equals(selectedItem.getText())) {
            userFilterDropdownMenu.setText("Filter Books");
        } else {
            userFilterDropdownMenu.setText("Filter Books: " + selectedItem.getText());
        }
    }

    private void updateTableViewItems() {
        if (userSearchTextField.getText().isEmpty() && viewModelUser.getGenreFilter().isEmpty()) {
            bookTableViewUser.setItems(viewModelUser.getBooks());
        } else {
            SortedList<Book> sortedBooks = new SortedList<>(viewModelUser.getFilteredBooks());
            sortedBooks.comparatorProperty().bind(bookTableViewUser.comparatorProperty());
            bookTableViewUser.setItems(sortedBooks);
        }
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to borrow this book?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    viewModelUser.borrowBook(selectedBook, currentUser);
                }
            });
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return this book?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    viewModelUser.returnBook(selectedBook, currentUser);
                }
            });
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to reserve this book?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    viewModelUser.reserveBook(selectedBook, currentUser);
                }
            });
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel the reservation for this book?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    viewModelUser.cancelReservation(selectedBook, currentUser);
                }
            });
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Opens My profile view
    @FXML
    private void handleOnClickOpenMyProfile() {
        SceneManager.showMyProfile(SessionManager.getInstance().getCurrentUser());
    }
}