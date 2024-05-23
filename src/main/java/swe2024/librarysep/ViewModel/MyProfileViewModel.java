package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SessionManager;
import swe2024.librarysep.Utility.SceneManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel for the MyProfile view.
 * Manages the user's profile information and book data displayed in the profile.
 */
public class MyProfileViewModel {
    private StringProperty username = new SimpleStringProperty();
    private ObservableList<Book> userBooks = FXCollections.observableArrayList();
    private final BookService bookService;

    /**
     * Constructs a MyProfileViewModel with the specified {@link BookService}.
     *
     * @param bookService the {@link BookService} used to fetch user's books
     * @throws SQLException    if an SQL exception occurs
     * @throws RemoteException if a remote exception occurs
     */
    public MyProfileViewModel(BookService bookService) throws SQLException, RemoteException {
        this.bookService = bookService;
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            this.username.set(currentUser.getUsername());
            fetchUserBooks(currentUser);
        } else {
            this.username.set("Unknown");
        }
    }

    /**
     * Fetches the books belonging to the current user.
     *
     * @param currentUser the current user
     * @throws SQLException    if an SQL exception occurs
     * @throws RemoteException if a remote exception occurs
     */
    private void fetchUserBooks(User currentUser) throws SQLException, RemoteException {
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> currentUser.getUsername().equals(book.getUsername()))
                .collect(Collectors.toList());
        userBooks.setAll(filteredBooks);
    }

    /**
     * Gets the username property.
     *
     * @return the username property
     */
    public StringProperty usernameProperty() {
        return username;
    }

    /**
     * Gets the list of user's books.
     *
     * @return the list of user's books
     */
    public ObservableList<Book> getUserBooks() {
        return userBooks;
    }

    /**
     * Binds profile table columns to the properties of the Book objects.
     *
     * @param titleColumn      the table column for book titles
     * @param authorColumn     the table column for book authors
     * @param releaseYearColumn the table column for book release years
     * @param stateColumn      the table column for book states
     * @param genreColumn      the table column for book genres
     */
    public void bindProfileTableColumns(
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

    /**
     * Navigates back to the appropriate dashboard based on the user's role.
     */
    public void handleBackToDashboard() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && "Admin".equals(currentUser.getUsername())) {
            SceneManager.showAdminDashboard();
        } else {
            SceneManager.showUserDashboard();
        }
    }
}

