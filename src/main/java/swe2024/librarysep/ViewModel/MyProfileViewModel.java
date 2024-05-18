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

public class MyProfileViewModel {
    private StringProperty username = new SimpleStringProperty();
    private ObservableList<Book> userBooks = FXCollections.observableArrayList();
    private BookService bookService;

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

    private void fetchUserBooks(User currentUser) throws SQLException, RemoteException {
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> currentUser.getUsername().equals(book.getUsername()))
                .collect(Collectors.toList());
        userBooks.setAll(filteredBooks);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public ObservableList<Book> getUserBooks() {
        return userBooks;
    }

    // Bind properties
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

    public void handleBackToDashboard() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && "Admin".equals(currentUser.getUsername())) {
            SceneManager.showAdminDashboard();
        } else {
            SceneManager.showUserDashboard();
        }
    }
}

