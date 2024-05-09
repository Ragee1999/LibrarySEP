package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

// Since the project is time constrained for speeding up the process we have imported both Book and database into view
// It goes against MVVM principles but for now this is a quick solution for time saving
// We decided to make this exception in this case to import from Model (Mainly to update the GUI with data)
// If we have time over we will put time into fixing this, as few attempts has been waste of time
import swe2024.librarysep.Database.DatabaseService; // Technical debt
import swe2024.librarysep.Model.Book; // Technical debt


import swe2024.librarysep.ViewModel.DashboardViewModel;

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

    // Declaration of DashboardViewModel Instance
    private final DashboardViewModel viewModel;

    // Constructor for DashboardController initializing the DashboardViewModel with a DatabaseService instance
    //  enabling the ViewModel to interact with data from the database.
    public DashboardController() {
        this.viewModel = new DashboardViewModel(new DatabaseService());
    }

    @FXML
    public void initialize() {
        // Initialize TableView and propertybind
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateName"));

        // Fills TableView with data from Dashboard ViewModel
        bookTableView.setItems(viewModel.getBooks());
    }


    // Button action event for borrow
    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                selectedBook.borrow();
                viewModel.updateBookState(selectedBook);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No book selected");
        }
    }

    // Button action event for reservation
    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                selectedBook.reserve();
                viewModel.updateBookState(selectedBook);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No book selected");
        }
    }

    // Button action event for returning a book
    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                selectedBook.returnBook();
                viewModel.updateBookState(selectedBook);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No book selected");
        }
    }

    // Button action event for cancelling a reservation
    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                selectedBook.cancelReservation();
                viewModel.updateBookState(selectedBook);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No book selected");
        }
    }
}