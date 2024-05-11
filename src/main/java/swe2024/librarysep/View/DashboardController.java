package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe2024.librarysep.Model.Book;
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
    private DashboardViewModel viewModel;


    public void setViewModel(DashboardViewModel viewModel) {
        this.viewModel = viewModel;
        bookTableView.setItems(viewModel.getBooks());
        viewModel.bindTableColumns(titleColumn, authorColumn, releaseYearColumn, idColumn, stateColumn);
    }


    // Button action event for borrow
    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            viewModel.borrowBook(selectedBook);
        } else {
            System.out.println("No book selected");
        }
    }


    // Button action event for reservation
    @FXML
    private void handleReserveBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            viewModel.reserveBook(selectedBook);
        } else {
            System.out.println("No book selected");
        }
    }


    // Button action event for returning a book
    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            viewModel.returnBook(selectedBook);
        } else {
            System.out.println("No book selected");
        }
    }


    // Button action event for cancelling a reservation
    @FXML
    private void handleCancelBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            viewModel.cancelReservation(selectedBook);
        } else {
            System.out.println("No book selected");
        }
    }
}