package swe2024.librarysep.ViewModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import swe2024.librarysep.Model.*;
import swe2024.librarysep.Utility.ObserverManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/*
TODO: Implement a design pattern to reduce code duplication between the admin
TODO: and user dashboard setup, this includes the relevant two controllers and two viewmodels
*/

/**
 * ViewModel class for the Admin Dashboard in the library system.
 * This class manages the state and behavior of the Admin Dashboard,
 * including loading books, filtering books, handling user actions
 * such as borrowing, returning, reserving, and deleting books.
 */

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

    /**
     * Constructor to initialize the AdminDashboardViewModel with a BookService instance.
     * It sets up observers and listeners for filtering books.
     *
     * @param bookService the service to interact with book data
     * @throws SQLException if there is an error accessing the database
     * @throws RemoteException if there is an error with remote communication
     */
    public AdminDashboardViewModel(BookService bookService) throws SQLException, RemoteException {
        this.bookService = bookService;
        loadBooks();

        // Observer setup
        ObserverManager observerManager = new ObserverManager(this);
        bookService.addObserver(observerManager);

        filteredBooks = new FilteredList<>(books, book -> true);

        searchQuery.addListener((observable, oldValue, newValue) -> updateFilter());
        genreFilter.addListener((observable, oldValue, newValue) -> updateFilter());
    }

    /**
     * Gets the observable list of books.
     *
     * @return the observable list of books
     */
    public ObservableList<Book> getBooks() {
        return books;
    }

    /**
     * Refreshes the list of books by reloading them from the book service.
     *
     * @throws RemoteException if there is an error with remote communication
     */
    public void refreshBooks() throws RemoteException {
        try {
            loadBooks();
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    /**
     * Updates the filter applied to the list of books based on search query and genre filter.
     */
    private void updateFilter() {
        filteredBooks.setPredicate(book -> {
            boolean matchesSearchQuery = searchQuery.get() == null || searchQuery.get().isEmpty() ||
                    book.getTitle().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getReleaseYear().toString().contains(searchQuery.get().toLowerCase()) ||
                    book.getGenre().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getStateName().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    // Admin specific
                    book.getBookId().toString().contains(searchQuery.get().toLowerCase()) ||
                    book.getUsername().toLowerCase().contains(searchQuery.get().toLowerCase());

            boolean matchesGenreFilter = genreFilter.get() == null || genreFilter.get().isEmpty() ||
                    book.getGenre().equalsIgnoreCase(genreFilter.get());
            return matchesSearchQuery && matchesGenreFilter;
        });
    }

    /**
     * Binds the table columns to the properties of the Book class.
     *
     * @param titleColumn the title column
     * @param authorColumn the author column
     * @param releaseYearColumn the release year column
     * @param idColumn the ID column
     * @param stateColumn the state column
     * @param clientColumn the client (username) column
     * @param genreColumn the genre column
     */
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
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
    }

    /**
     * Loads the list of books from the book service and updates the observable list.
     *
     * @throws SQLException if there is an error accessing the database
     * @throws RemoteException if there is an error with remote communication
     */
    public void loadBooks() throws SQLException, RemoteException {
        List<Book> updatedBooks = bookService.getAllBooks();

        books.removeIf(book -> updatedBooks.stream()
                .noneMatch(updatedBook -> updatedBook.getBookId().equals(book.getBookId())));

        for (Book updatedBook : updatedBooks) {
            boolean found = false;
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookId().equals(updatedBook.getBookId())) {
                    books.set(i, updatedBook);
                    found = true;
                    break;
                }
            }
            if (!found) {
                books.add(updatedBook);
            }
        }
    }

    /**
     * Updates the state of a given book and reloads the list of books.
     *
     * @param book the book to update
     * @throws SQLException if there is an error accessing the database
     * @throws RemoteException if there is an error with remote communication
     */
    public void updateBookState(Book book) throws SQLException, RemoteException {
        bookService.updateBookState(book);
        loadBooks();
    }

    /**
     * Gets the filtered list of books based on the search query and genre filter.
     *
     * @return the filtered list of books
     */
    public ObservableList<Book> getFilteredBooks() {
        return filteredBooks;
    }

    /**
     * Sets the search query for filtering books.
     *
     * @param searchQuery the search query
     */
    public void setSearchQuery(String searchQuery) {
        this.searchQuery.set(searchQuery == null ? "" : searchQuery);
    }

    /**
     * Sets the genre filter for filtering books.
     *
     * @param genre the genre filter
     */
    public void setGenreFilter(String genre) {
        this.genreFilter.set(genre == null ? "" : genre);
    }

    /**
     * Gets the genre filter.
     *
     * @return the genre filter
     */
    public String getGenreFilter() {
        return genreFilter.get() == null ? "" : genreFilter.get();
    }

    /**
     * Gets the list of available genres for filtering.
     *
     * @return the list of genres
     */
    public List<String> getGenres() {
        return List.of("Fiction", "Science Fiction", "Romance", "Political Satire",
                "Fantasy", "Modernist", "Gothic", "Adventure", "Satire");
    }

    private StringProperty successMessage = new SimpleStringProperty();
    private StringProperty errorMessage = new SimpleStringProperty();

    /**
     * Gets the property for error messages to be bound to UI elements.
     *
     * @return the error message property
     */
    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    /**
     * Gets the property for success messages to be bound to UI elements.
     *
     * @return the success message property
     */
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

    /**
     * Requests confirmation for borrowing a book.
     *
     * @param book the book to be borrowed
     * @param user the user requesting the borrow
     */
    public void requestBorrowConfirmation(Book book, User user) {
        if (canBorrow(book, user)) {
            selectedBook = book;
            currentUser = user;
            borrowConfirmationRequested.set(true);
        }
    }

    /**
     * Requests confirmation for returning a book.
     *
     * @param book the book to be returned
     * @param user the user requesting the return
     */
    public void requestReturnConfirmation(Book book, User user) {
        if (canReturn(book, user)) {
            selectedBook = book;
            currentUser = user;
            returnConfirmationRequested.set(true);
        }
    }

    /**
     * Requests confirmation for reserving a book.
     *
     * @param book the book to be reserved
     * @param user the user requesting the reservation
     */
    public void requestReserveConfirmation(Book book, User user) {
        if (canReserve(book, user)) {
            selectedBook = book;
            currentUser = user;
            reserveConfirmationRequested.set(true);
        }
    }

    /**
     * Requests confirmation for canceling a reservation.
     *
     * @param book the book to cancel reservation for
     * @param user the user requesting the cancellation
     */
    public void requestCancelReservationConfirmation(Book book, User user) {
        if (canCancelReservation(book, user)) {
            selectedBook = book;
            currentUser = user;
            cancelReservationConfirmationRequested.set(true);
        }
    }

    /**
     * Checks if a book can be borrowed by a user.
     *
     * @param book the book to check
     * @param user the user requesting to borrow the book
     * @return true if the book can be borrowed, false otherwise
     */
    private boolean canBorrow(Book book, User user) {
        if (book.getUsername() == null || book.getUsername().isEmpty() ||
                (book.getState() instanceof ReservedState && book.getUsername().equals(user.getUsername()))) {
            return true;
        } else if (book.getUsername().equals(user.getUsername())) {
            errorMessage.set("You already borrowed this book.");
        } else {
            errorMessage.set("Book is already borrowed by another user.");
        }
        return false;
    }

    /**
     * Checks if a book can be returned by a user.
     *
     * @param book the book to check
     * @param user the user requesting to return the book
     * @return true if the book can be returned, false otherwise
     */
    private boolean canReturn(Book book, User user) {
        if (book.getUsername() != null && book.getUsername().equals(user.getUsername()) && book.getState() instanceof BorrowedState) {
            return true;
        }
        errorMessage.set("Book is either not borrowed by the current user or is not in a borrowed state.");
        return false;
    }

    /**
     * Checks if a book can be reserved by a user.
     *
     * @param book the book to check
     * @param user the user requesting to reserve the book
     * @return true if the book can be reserved, false otherwise
     */
    private boolean canReserve(Book book, User user) {
        if (book.getState() instanceof AvailableState) {
            return true;
        }
        errorMessage.set("Book is not available for reservation.");
        return false;
    }

    /**
     * Checks if a book reservation can be canceled by a user.
     *
     * @param book the book to check
     * @param user the user requesting to cancel the reservation
     * @return true if the reservation can be canceled, false otherwise
     */
    private boolean canCancelReservation(Book book, User user) {
        if (book.getUsername() != null && book.getUsername().equals(user.getUsername()) && book.getState() instanceof ReservedState) {
            return true;
        }
        errorMessage.set("You are not the one who reserved this book or it's not reserved.");
        return false;
    }

    /**
     * Borrows a book for the current user.
     */
    public void borrowBook() {
        try {
            selectedBook.borrow();
            selectedBook.setUserId(currentUser.getUserId());
            selectedBook.setUsername(currentUser.getUsername());
            updateBookState(selectedBook);
            successMessage.set("Book borrowed successfully!");
        } catch (IllegalStateException | SQLException | RemoteException e) {
            errorMessage.set("Error borrowing book: " + e.getMessage());
        }
    }

    /**
     * Returns a book from the current user.
     */
    public void returnBook() {
        try {
            selectedBook.returnBook();
            selectedBook.setUsername(null);
            updateBookState(selectedBook);
            successMessage.set("Book returned successfully!");
        } catch (IllegalStateException | SQLException | RemoteException e) {
            errorMessage.set("Error returning book: " + e.getMessage());
        }
    }

    /**
     * Reserves a book for the current user.
     */
    public void reserveBook() {
        try {
            selectedBook.reserve();
            selectedBook.setUserId(currentUser.getUserId());
            selectedBook.setUsername(currentUser.getUsername());
            updateBookState(selectedBook);
            successMessage.set("Book reserved successfully!");
        } catch (IllegalStateException | SQLException | RemoteException e) {
            errorMessage.set("Error reserving book: " + e.getMessage());
        }
    }

    /**
     * Cancels a reservation for a book by the current user.
     */
    public void cancelReservation() {
        try {
            selectedBook.cancelReservation();
            selectedBook.setUsername(null);
            updateBookState(selectedBook);
            successMessage.set("Reservation cancelled successfully!");
        } catch (IllegalStateException | SQLException | RemoteException e) {
            errorMessage.set("Error cancelling reservation: " + e.getMessage());
        }
    }

    /**
     * Deletes a book from the system.
     *
     * @param book the book to be deleted
     */
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

