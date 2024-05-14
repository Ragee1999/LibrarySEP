package swe2024.librarysep.ViewModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

public class DashboardViewModel {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private BookService bookService;
    private Timeline refresh;
    private FilteredList<Book> filteredBooks;
    private StringProperty searchQuery = new SimpleStringProperty("");
    private StringProperty genreFilter = new SimpleStringProperty(null);

    public DashboardViewModel(BookService bookService) {
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

        // Update the filtered list whenever the search query changes
//        searchQuery.addListener((observable, oldValue, newValue) -> {
//            filteredBooks.setPredicate(book -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String lowerCaseQuery = newValue.toLowerCase();
//                return book.getTitle().toLowerCase().contains(lowerCaseQuery)
//                        || book.getAuthor().toLowerCase().contains(lowerCaseQuery);
//            });
//        });
    }

    private void updateFilter() {
        filteredBooks.setPredicate(book -> {
            boolean matchesSearchQuery = searchQuery.get() == null || searchQuery.get().isEmpty() ||
                    book.getTitle().toLowerCase().contains(searchQuery.get().toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchQuery.get().toLowerCase());
            boolean matchesGenreFilter = genreFilter.get() == null || genreFilter.get().isEmpty() ||
                    book.getGenre().equalsIgnoreCase(genreFilter.get());
            return matchesSearchQuery && matchesGenreFilter;
        });
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

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery.set(searchQuery);
    }

    public StringProperty genreFilterProperty() {
        return genreFilter;
    }

    public void setGenreFilter(String genre) {
        this.genreFilter.set(genre);
    }

    public List<String> getGenres() {
        return List.of("Fiction", "Science Fiction", "Romance", "Political Satire", "Fantasy", "Modernist", "Gothic", "Adventure", "Satire");
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
