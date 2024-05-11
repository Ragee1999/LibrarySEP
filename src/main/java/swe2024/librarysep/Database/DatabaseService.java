package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


// This class provides database services for interacting with book data.
// It implements the BookService interface.

public class DatabaseService implements BookService {
    private final Connection connection; // Declare a single connection object


// Constructor for DatabaseService.
// Initializes the connection to the database.

    public DatabaseService() {
        try {
            this.connection = DatabaseConnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }


// Retrieves a list of all books from the database making it into java objects in this case Books.

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT bookId, title, author, releaseYear, state FROM books");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("bookId"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getInt("releaseYear"));
                book.setState(Book.getStateFromString(resultSet.getString("state")));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
        }
        return books;
    }


// Updates the state of a book in the database.

    @Override
    public void updateBookState(Book book) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE books SET state = ? WHERE bookId = ?");
            statement.setString(1, book.getStateName());
            statement.setInt(2, book.getBookId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating the book failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating book state in database: " + e.getMessage());
        }
    }
}


