package swe2024.librarysep.ViewModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import swe2024.librarysep.Model.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboardViewModel {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private BookService bookService;
    private FilteredList<Book> filteredBooks;
    private StringProperty searchQuery = new SimpleStringProperty("");
    private StringProperty genreFilter = new SimpleStringProperty(null);

    private BooleanProperty borrowConfirmationRequested = new SimpleBooleanProperty(false);
    private BooleanProperty returnConfirmationRequested = new SimpleBooleanProperty(false);
    private BooleanProperty reserveConfirmationRequested = new SimpleBooleanProperty(false);
    private BooleanProperty cancelReservationConfirmationRequested = new SimpleBooleanProperty(false);
    private Book selectedBook;
    private User currentUser;

    public AdminDashboardViewModel(BookService bookService) {
        this.bookService = bookService;
        loadBooks();
        setupRefresh();
        filteredBooks = new FilteredList<>(books, book -> true);

        searchQuery.addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });

        genreFilter.addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });
    }

    public ObservableList<Book> getBooks() {
        return books;
    }

// Admin can additionally search by book id and state names, which they can't in the user dashboard
    private void updateFilter() {
        filteredBooks.setPredicate(book -> {
            boolean matchesSearchQuery = searchQuery.get() == null || searchQuery.get().isEmpty() ||
                    book.getTitle().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getReleaseYear().toString().contains(searchQuery.get().toLowerCase()) ||
                    book.getGenre().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getStateName().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getBookId().toString().contains(searchQuery.get().toLowerCase());
            boolean matchesGenreFilter = genreFilter.get() == null || genreFilter.get().isEmpty() ||
                    book.getGenre().equalsIgnoreCase(genreFilter.get());
            return matchesSearchQuery && matchesGenreFilter;
        });
    }

    private void setupRefresh() {
        Timeline refresh = new Timeline(new KeyFrame(Duration.seconds(15), event -> loadBooks()));
        refresh.setCycleCount(Timeline.INDEFINITE);
        refresh.play();
    }

    // Bind properties
    public void bindTableColumns(
            TableColumn<Book, String> titleColumn,
            TableColumn<Book, String> authorColumn,
            TableColumn<Book, Integer> releaseYearColumn,
            TableColumn<Book, Integer> idColumn,
            TableColumn<Book, String> stateColumn,
            TableColumn<Book, String> clientColumn,
            TableColumn<Book, String> genreColumn
    ) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateName"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
    }

    private void loadBooks() { // Checks for accidentally duplicated books and updates after state change, so we don't ruin the database
        List<Book> updatedBooks = bookService.getAllBooks();

        books.removeIf(book -> updatedBooks.stream()
                .noneMatch(updatedBook -> updatedBook.getBookId().equals(book.getBookId())));

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

    public ObservableList<Book> getFilteredBooks() {
        return filteredBooks;
    }


    public void setSearchQuery(String searchQuery) {
        this.searchQuery.set(searchQuery == null ? "" : searchQuery);
    }


    public void setGenreFilter(String genre) {
        this.genreFilter.set(genre == null ? "" : genre);
    }

    public String getGenreFilter() {
        return genreFilter.get() == null ? "" : genreFilter.get();
    }

    public List<String> getGenres() {
        return List.of("Fiction", "Science Fiction", "Romance", "Political Satire",
                "Fantasy", "Modernist", "Gothic", "Adventure", "Satire");
    }

    // Bind errorMessage for the UI alerts
    private StringProperty successMessage = new SimpleStringProperty();
    private StringProperty errorMessage = new SimpleStringProperty();

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public StringProperty successMessageProperty() {
        return successMessage;
    }

    public BooleanProperty borrowConfirmationRequestedProperty() {
        return borrowConfirmationRequested;
    }

    public BooleanProperty returnConfirmationRequestedProperty() {
        return returnConfirmationRequested;
    }

    public BooleanProperty reserveConfirmationRequestedProperty() {
        return reserveConfirmationRequested;
    }

    public BooleanProperty cancelReservationConfirmationRequestedProperty() {
        return cancelReservationConfirmationRequested;
    }

    public void requestBorrowConfirmation(Book book, User user) {
        if (canBorrow(book, user)) {
            selectedBook = book;
            currentUser = user;
            borrowConfirmationRequested.set(true);
        }
    }

    public void requestReturnConfirmation(Book book, User user) {
        if (canReturn(book, user)) {
            selectedBook = book;
            currentUser = user;
            returnConfirmationRequested.set(true);
        }
    }

    public void requestReserveConfirmation(Book book, User user) {
        if (canReserve(book, user)) {
            selectedBook = book;
            currentUser = user;
            reserveConfirmationRequested.set(true);
        }
    }

    public void requestCancelReservationConfirmation(Book book, User user) {
        if (canCancelReservation(book, user)) {
            selectedBook = book;
            currentUser = user;
            cancelReservationConfirmationRequested.set(true);
        }
    }

    private boolean canBorrow(Book book, User user) {
        if (book.getUserName() == null || book.getUserName().isEmpty() ||
                (book.getState() instanceof ReservedState && book.getUserName().equals(user.getUsername()))) {
            return true;
        } else if (book.getUserName().equals(user.getUsername())) {
            errorMessage.set("You already borrowed this book.");
        } else {
            errorMessage.set("Book is already borrowed by another user.");
        }
        return false;
    }

    private boolean canReturn(Book book, User user) {
        if (book.getUserName() != null && book.getUserName().equals(user.getUsername()) && book.getState() instanceof BorrowedState) {
            return true;
        }
        errorMessage.set("Book is either not borrowed by the current user or is not in a borrowed state.");
        return false;
    }

    private boolean canReserve(Book book, User user) {
        if (book.getState() instanceof AvailableState) {
            return true;
        }
        errorMessage.set("Book is not available for reservation.");
        return false;
    }

    private boolean canCancelReservation(Book book, User user) {
        if (book.getUserName() != null && book.getUserName().equals(user.getUsername()) && book.getState() instanceof ReservedState) {
            return true;
        }
        errorMessage.set("You are not the one who reserved this book or it's not reserved.");
        return false;
    }

    public void borrowBook() {
        try {
            selectedBook.borrow();
            selectedBook.setUserId(currentUser.getUserId());
            selectedBook.setUserName(currentUser.getUsername());
            updateBookState(selectedBook);
            successMessage.set("Book borrowed successfully!");
        } catch (IllegalStateException e) {
            errorMessage.set("Error borrowing book: " + e.getMessage());
        }
    }

    public void returnBook() {
        try {
            selectedBook.returnBook();
            selectedBook.setUserName(null);
            updateBookState(selectedBook);
            successMessage.set("Book returned successfully!");
        } catch (IllegalStateException e) {
            errorMessage.set("Error returning book: " + e.getMessage());
        }
    }

    public void reserveBook() {
        try {
            selectedBook.reserve();
            selectedBook.setUserId(currentUser.getUserId());
            selectedBook.setUserName(currentUser.getUsername());
            updateBookState(selectedBook);
            successMessage.set("Book reserved successfully!");
        } catch (IllegalStateException e) {
            errorMessage.set("Error reserving book: " + e.getMessage());
        }
    }

    public void cancelReservation() {
        try {
            selectedBook.cancelReservation();
            selectedBook.setUserName(null);
            updateBookState(selectedBook);
            successMessage.set("Reservation cancelled successfully!");
        } catch (IllegalStateException e) {
            errorMessage.set("Error cancelling reservation: " + e.getMessage());
        }
    }

    public void deleteBook(Book book) {
        if (book != null) {
            try {
                bookService.deleteBook(book.getBookId());
                books.remove(book);  // Remove the book from the observable list to update UI
            } catch (SQLException e) {
                errorMessage.set("Failed to delete book: " + e.getMessage());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorMessage.set("No book selected to delete.");
        }
    }
}
