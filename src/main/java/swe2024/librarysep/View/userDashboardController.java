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

        // Bind confirmation properties to show dialogs
        viewModelUser.borrowConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to borrow this book?", viewModelUser::borrowBook);
                viewModelUser.borrowConfirmationRequestedProperty().set(false);
            }
        });

        viewModelUser.returnConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to return this book?", viewModelUser::returnBook);
                viewModelUser.returnConfirmationRequestedProperty().set(false);
            }
        });

        viewModelUser.reserveConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to reserve this book?", viewModelUser::reserveBook);
                viewModelUser.reserveConfirmationRequestedProperty().set(false);
            }
        });

        viewModelUser.cancelReservationConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to cancel the reservation for this book?", viewModelUser::cancelReservation);
                viewModelUser.cancelReservationConfirmationRequestedProperty().set(false);
            }
        });

        // Inserts genres into the filter dropdown menu
        for (String genre : viewModelUser.getGenres()) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(event -> {
                viewModelUser.setGenreFilter(genre);
                highlightSelectedItem(item);
                updateTableViewItems();
            });
            item.getStyleClass().add("menu-item-default"); // adds the color to all the dropdown menus
            userFilterDropdownMenu.getItems().add(item);
        }

        // Adds a Clear Filter button item in dropdown menu and applies the css to it
        MenuItem clearFilter = new MenuItem("Clear Filter");
        clearFilter.setOnAction(event -> {
            viewModelUser.setGenreFilter(null);
            highlightSelectedItem(clearFilter);
            updateTableViewItems();
        });
        clearFilter.getStyleClass().add("menu-item-default");
        userFilterDropdownMenu.getItems().add(clearFilter);
        highlightSelectedItem(clearFilter);
    }

    private void highlightSelectedItem(MenuItem selectedItem) {
        currentSelectedItem = selectedItem;
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
            viewModelUser.requestBorrowConfirmation(selectedBook, currentUser);
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.requestReturnConfirmation(selectedBook, currentUser);
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.requestReserveConfirmation(selectedBook, currentUser);
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableViewUser.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModelUser.requestCancelReservationConfirmation(selectedBook, currentUser);
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

    private void showConfirmation(String message, Runnable onConfirm) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                onConfirm.run();
            }
        });
    }

    // Opens My profile view
    @FXML
    private void handleOnClickOpenMyProfile() {
        SceneManager.showMyProfile(SessionManager.getInstance().getCurrentUser());
    }
}