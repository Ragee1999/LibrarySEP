package swe2024.librarysep.View;

import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.Utility.SessionManager;
import swe2024.librarysep.ViewModel.UserDashboardViewModel;

/**
 * This class handles user interactions and binds the UI components to the UserDashboardViewModel.
 */
public class UserDashboardView {

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

    private UserDashboardViewModel viewModelUser;
    private MenuItem currentSelectedItem;

    /**
     * Sets the ViewModel for this view and binds UI components to it.
     *
     * @param viewModelUser the ViewModel instance to bind
     */
    public void setViewModel(UserDashboardViewModel viewModelUser) {
        this.viewModelUser = viewModelUser;
        bookTableViewUser.setItems(viewModelUser.getBooks());
        viewModelUser.bindTableColumns(titleColumn, authorColumn, releaseYearColumn, stateColumn, genreColumn);

        // Bind search text field to update the filter predicate
        userSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            viewModelUser.setUserSearchQuery(newValue);
            updateTableViewItems();
        });

        // Bind error message property to show alerts on changes
        this.viewModelUser.errorMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showAlert(newValue, Alert.AlertType.ERROR);
                this.viewModelUser.errorMessageProperty().set(""); // Reset the message to prevent repeated alerts
            }
        });

        // Bind success message property to show alerts on changes
        this.viewModelUser.successMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showAlert(newValue, Alert.AlertType.INFORMATION);
                this.viewModelUser.successMessageProperty().set(""); // Reset the message to prevent repeated alerts
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

        // Populate filter dropdown menu with genres
        for (String genre : viewModelUser.getGenres()) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(event -> {
                viewModelUser.setGenreFilter(genre);
                highlightSelectedItem(item);
                updateTableViewItems();
            });
            item.getStyleClass().add("menu-item-default"); // Adds the color to all the dropdown menus
            userFilterDropdownMenu.getItems().add(item);
        }

        // Add a Clear Filter button item in dropdown menu and apply the CSS to it
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

    /**
     * Highlights the currently selected item in the filter dropdown menu.
     *
     * @param selectedItem the item to highlight
     */
    private void highlightSelectedItem(MenuItem selectedItem) {
        currentSelectedItem = selectedItem;
    }

    /**
     * Updates the items in the TableView based on the current search query and genre filter.
     */
    private void updateTableViewItems() {
        if (userSearchTextField.getText().isEmpty() && viewModelUser.getGenreFilter().isEmpty()) {
            bookTableViewUser.setItems(viewModelUser.getBooks());
        } else {
            SortedList<Book> sortedBooks = new SortedList<>(viewModelUser.getFilteredBooks());
            sortedBooks.comparatorProperty().bind(bookTableViewUser.comparatorProperty());
            bookTableViewUser.setItems(sortedBooks);
        }
    }

    /**
     * Handles the action of borrowing a book.
     * Shows an alert if no book is selected.
     */
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

    /**
     * Handles the action of returning a book.
     * Shows an alert if no book is selected.
     */
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

    /**
     * Handles the action of reserving a book.
     * Shows an alert if no book is selected.
     */
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

    /**
     * Handles the action of canceling a book reservation.
     * Shows an alert if no book is selected.
     */
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

    /**
     * Shows an alert with the specified message and alert type.
     *
     * @param message the message to display
     * @param alertType the type of alert to show
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog with the specified message and action on confirmation.
     *
     * @param message the message to display
     * @param onConfirm the action to perform on confirmation
     */
    private void showConfirmation(String message, Runnable onConfirm) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                onConfirm.run();
            }
        });
    }

    /**
     * Opens the My Profile view.
     */
    @FXML
    private void handleOnClickOpenMyProfile() {
        SceneManager.showMyProfile(SessionManager.getInstance().getCurrentUser());
    }
}
