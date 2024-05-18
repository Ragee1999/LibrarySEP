package swe2024.librarysep.View;

import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.ViewModel.AdminDashboardViewModel;
import swe2024.librarysep.Utility.SessionManager;


public class AdminDashboardController {
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
    @FXML
    private TextField searchTextField;
    @FXML
    private MenuButton filterDropdownMenu;

    // Declaration of DashboardViewModel Instance
    private AdminDashboardViewModel viewModel;

    private MenuItem currentSelectedItem;

    public void setViewModel(AdminDashboardViewModel viewModel) {
        this.viewModel = viewModel;
        bookTableView.setItems(viewModel.getBooks());
        viewModel.bindTableColumns(titleColumn, authorColumn, releaseYearColumn, idColumn, stateColumn, clientColumn, genreColumn);

        // Bind search text field to update the filter
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSearchQuery(newValue);
            updateTableViewItems();
        });

        // Binds the error message property to show alerts on changes
        this.viewModel.errorMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showAlert(newValue, Alert.AlertType.ERROR);
                this.viewModel.errorMessageProperty().set(""); // Also resets the message to prevent repeated alerts
            }
        });

        // Binds the success message property to show alerts on changes
        this.viewModel.successMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                showAlert(newValue, Alert.AlertType.INFORMATION);
                this.viewModel.successMessageProperty().set(""); // Also resets the message to prevent repeated alerts
            }
        });

        // Bind confirmation properties to show dialogs
        viewModel.borrowConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to borrow this book?", viewModel::borrowBook);
                viewModel.borrowConfirmationRequestedProperty().set(false);
            }
        });

        viewModel.returnConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to return this book?", viewModel::returnBook);
                viewModel.returnConfirmationRequestedProperty().set(false);
            }
        });

        viewModel.reserveConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to reserve this book?", viewModel::reserveBook);
                viewModel.reserveConfirmationRequestedProperty().set(false);
            }
        });

        viewModel.cancelReservationConfirmationRequestedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showConfirmation("Are you sure you want to cancel the reservation for this book?", viewModel::cancelReservation);
                viewModel.cancelReservationConfirmationRequestedProperty().set(false);
            }
        });

        // Inserts genres into the filter dropdown menu
        for (String genre : viewModel.getGenres()) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(event -> {
                viewModel.setGenreFilter(genre);
                highlightSelectedItem(item);
                updateTableViewItems();
            });
            item.getStyleClass().add("menu-item-default");
            filterDropdownMenu.getItems().add(item);
        }

        // Clear Filter button item in dropdown menu
        MenuItem clearFilter = new MenuItem("Clear Filter");
        clearFilter.setOnAction(event -> {
            viewModel.setGenreFilter(null);
            highlightSelectedItem(clearFilter);
            updateTableViewItems();
        });
        clearFilter.getStyleClass().add("menu-item-default");
        filterDropdownMenu.getItems().add(clearFilter);
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
            filterDropdownMenu.setText("Filter Books");
        } else {
            filterDropdownMenu.setText("Filter Books: " + selectedItem.getText());
        }
    }

    private void updateTableViewItems() {
        if (searchTextField.getText().isEmpty() && viewModel.getGenreFilter().isEmpty()) {
            bookTableView.setItems(viewModel.getBooks());
        } else {
            SortedList<Book> sortedBooks = new SortedList<>(viewModel.getFilteredBooks());
            sortedBooks.comparatorProperty().bind(bookTableView.comparatorProperty());
            bookTableView.setItems(sortedBooks);
        }
    }


    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.requestBorrowConfirmation(selectedBook, currentUser);
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.requestReturnConfirmation(selectedBook, currentUser);
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.requestReserveConfirmation(selectedBook, currentUser);
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (selectedBook != null && currentUser != null) {
            viewModel.requestCancelReservationConfirmation(selectedBook, currentUser);
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

    /*----------------------------*/
    //  ADMIN SPECIFIC FEATURES   //
    /*----------------------------*/

    // Opens Add book view
    @FXML
    private void handleOnClickAddBook() {
        SceneManager.showAddBook();
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
                        showAlert("Failed to delete book: " + e.getCause().getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            showAlert("No book selected.", Alert.AlertType.ERROR);
        }
    }

    // Selects a book to edit and then opens the edit book UI.
    @FXML
    private void handleOnClickEditBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            SceneManager.showEditBook(selectedBook);
        } else {
            showEditButtonAlert("No Selection", "No book selected. Please select a book to edit.");
        }
    }

    // Specific alert for edit book Button
    private void showEditButtonAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
