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

//
// This class is almost similar to DashboardViewModel, the only difference being constructor having 2 fewer columns and property-bindings
//


public class UserDashboardViewModel {

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private BookService bookService;
    private FilteredList<Book> filteredBooks;
    private StringProperty userSearchQuery = new SimpleStringProperty("");
    private StringProperty userGenreFilter = new SimpleStringProperty(null);

    private BooleanProperty borrowConfirmationRequested = new SimpleBooleanProperty(false);
    private BooleanProperty returnConfirmationRequested = new SimpleBooleanProperty(false);
    private BooleanProperty reserveConfirmationRequested = new SimpleBooleanProperty(false);
    private BooleanProperty cancelReservationConfirmationRequested = new SimpleBooleanProperty(false);
    private Book selectedBook;
    private User currentUser;

    public UserDashboardViewModel(BookService bookService) throws SQLException, RemoteException {
        super();
        this.bookService = bookService;

        ObserverManager observerManager = new ObserverManager(this);
        bookService.addObserver(observerManager);


        loadBooks();
        filteredBooks = new FilteredList<>(books, book -> true);

        userSearchQuery.addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });

        userGenreFilter.addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });
    }

    public void refreshBooks() throws RemoteException {
        try {
            loadBooks();
        } catch (SQLException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    private void updateFilter() {
        filteredBooks.setPredicate(book -> {
            boolean matchesSearchQuery = userSearchQuery.get() == null || userSearchQuery.get().isEmpty() ||
                    book.getTitle().toLowerCase().contains(userSearchQuery.get().toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(userSearchQuery.get().toLowerCase()) ||
                    book.getReleaseYear().toString().contains(userSearchQuery.get().toLowerCase()) ||
                    book.getGenre().toLowerCase().contains(userSearchQuery.get().toLowerCase());
            boolean matchesGenreFilter = userGenreFilter.get() == null || userGenreFilter.get().isEmpty() ||
                    book.getGenre().equalsIgnoreCase(userGenreFilter.get());
            return matchesSearchQuery && matchesGenreFilter;
        });
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

    private void loadBooks() throws SQLException, RemoteException { // Checks for accidentally duplicated books and updates after state change, so we don't ruin the database
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

    public void updateBookState(Book book) throws SQLException, RemoteException {
        bookService.updateBookState(book);
        loadBooks();
    }

    public ObservableList<Book> getFilteredBooks() {
        return filteredBooks;
    }


    public void setUserSearchQuery(String searchQuery) {
        this.userSearchQuery.set(searchQuery == null ? "" : searchQuery);
    }

    public void setGenreFilter(String genre) {
        this.userGenreFilter.set(genre == null ? "" : genre);
    }

    public String getGenreFilter() {
        return userGenreFilter.get() == null ? "" : userGenreFilter.get();
    }

    public List<String> getGenres() {
        return List.of("Fiction", "Science Fiction", "Romance", "Political Satire", "Fantasy", "Modernist", "Gothic", "Adventure", "Satire");
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

    private boolean canReturn(Book book, User user) {
        if (book.getUsername() != null && book.getUsername().equals(user.getUsername()) && book.getState() instanceof BorrowedState) {
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
        if (book.getUsername() != null && book.getUsername().equals(user.getUsername()) && book.getState() instanceof ReservedState) {
            return true;
        }
        errorMessage.set("You are not the one who reserved this book or it's not reserved.");
        return false;
    }

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
}
