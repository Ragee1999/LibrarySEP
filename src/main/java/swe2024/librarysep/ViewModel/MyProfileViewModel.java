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

import java.util.List;
import java.util.stream.Collectors;

public class MyProfileViewModel {
    private StringProperty username = new SimpleStringProperty();
    private User currentUser;
    private ObservableList<Book> userBooks = FXCollections.observableArrayList();
    private BookService bookService;

    public MyProfileViewModel(User currentUser, BookService bookService) {
        this.currentUser = currentUser;
        this.bookService = bookService;
        this.username.set(currentUser.getUsername());
        fetchUserBooks();
    }

    private void fetchUserBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> currentUser.getUsername().equals(book.getUserName()))
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
}


