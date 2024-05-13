package swe2024.librarysep.ViewModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import swe2024.librarysep.Model.*;
import java.util.List;

//
// This class is almost similar to DashboardViewModel, the only difference being constructor having 2 fewer columns and property-bindings
//


public class userDashboardViewModel {

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private BookService bookService;
    private Timeline refresh;

    public userDashboardViewModel(BookService bookService) {
        this.bookService = bookService;
        loadBooks();
        setupRefresh();
    }

    private void setupRefresh() {
        refresh = new Timeline(new KeyFrame(Duration.seconds(5), event -> loadBooks()));
        refresh.setCycleCount(Timeline.INDEFINITE);
        refresh.play();
    }

    public ObservableList<Book> getBooks() {
        return books;
    }

    // Bind properties
    public void bindTableColumns(
            TableColumn<Book, String> titleColumn,
            TableColumn<Book, String> authorColumn,
            TableColumn<Book, Integer> releaseYearColumn,
            TableColumn<Book, String> stateColumn,
            TableColumn<Book, String> genreColumn

    ) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
    }

    private void loadBooks() { // Checks for accidentally duplicated books and updates after state change, so we don't ruin the database
        List<Book> updatedBooks = bookService.getAllBooks();
        for (Book updatedBook : updatedBooks) {
            // Check if the book already exists in the list
            boolean found = false;
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookId().equals(updatedBook.getBookId())) {
                    books.set(i, updatedBook); // Update existing book
                    found = true;
                    break;
                }
            }
            if (!found) {
                books.add(updatedBook);
            }
        }
    }

    public void updateBookState(Book book) {
        bookService.updateBookState(book);
        loadBooks();
    }


    // Bind errorMessage for UI alerts
    private StringProperty errorMessage = new SimpleStringProperty();

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }


    public void borrowBook(Book book, User user) {
        try {
            // Check if the book is available (userName is null or empty)
            if (book.getUserName() == null || book.getUserName().isEmpty()) {
                // Set the user details and change the book state to borrowed
                book.setUserId(user.getUserId());
                book.setUserName(user.getUsername());
                book.borrow(); // This method should ideally only change the state of the book
                updateBookState(book); // Update book state in the database
            } else if (book.getState() instanceof ReservedState && book.getUserName().equals(user.getUsername())) {
                // Book is reserved by the current user, allow borrowing
                book.setUserId(user.getUserId());
                book.setUserName(user.getUsername());
                book.borrow(); // This method should ideally only change the state of the book
                updateBookState(book); // Update book state in the database
            } else if (book.getUserName().equals(user.getUsername())) {
                // Book is already borrowed by the current user
                errorMessage.set("Error borrowing book: You already borrowed this book.");

            } else {
                // Book is already borrowed by another user
                errorMessage.set("Error borrowing book: Book is already borrowed by another user.");
            }
        } catch (IllegalStateException e) {
            errorMessage.set("Error borrowing book: " + e.getMessage());
        }
    }

    public void returnBook(Book book, User user) {
        try {
            if (book.getUserName() != null && book.getUserName().equals(user.getUsername()) && book.getState() instanceof BorrowedState) {
                book.returnBook();
                book.setUserName(null); // Reset the username to null
                updateBookState(book); // Update book state in the database
            } else {
                errorMessage.set("Error returning book: Book is either not borrowed by the current user or is not in a borrowed state.");
            }
        } catch (IllegalStateException e) {
            errorMessage.set("Error returning book: " + e.getMessage());
        }
    }

    public void reserveBook(Book book, User user) {
        try {
            if (book.getState() instanceof AvailableState) {
                book.setUserId(user.getUserId());
                book.setUserName(user.getUsername());
                book.reserve(); // This method should ideally only change the state of the book
                updateBookState(book); // Update book state in the database
            } else {
                errorMessage.set("Error reserving book: Book is not available for reservation.");
            }
        } catch (IllegalStateException e) {
            errorMessage.set("Error reserving book: " + e.getMessage());
        }
    }

    public void cancelReservation(Book book, User user) {
        try {
            if (book.getUserName() != null && book.getUserName().equals(user.getUsername()) && book.getState() instanceof ReservedState) {
                book.cancelReservation(); // This method should ideally only change the state of the book
                book.setUserName(null); // Reset the username to null
                updateBookState(book); // Update book state in the database
            } else {
                errorMessage.set("Error cancelling reservation: You are not the one who reserved this book or it's not reserved.");
            }
        } catch (IllegalStateException e) {
            errorMessage.set("Error cancelling reservation: " + e.getMessage());
        }
    }
}
