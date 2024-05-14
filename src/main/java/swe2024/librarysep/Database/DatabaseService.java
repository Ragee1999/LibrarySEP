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


// CRUD RELATED METHODS BELOW


// Retrieves a list of all books from the database making it into java objects in this case Books.
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            // Updated SQL to include a join with the users table
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT b.bookId, b.title, b.author, b.releaseYear, b.state, b.genre, u.username AS userName " +
                            "FROM books b " +
                            "LEFT JOIN users u ON b.user_id = u.id"
            );
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("bookId"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("releaseYear"),
                        resultSet.getString("genre")
                );
                book.setState(Book.getStateFromString(resultSet.getString("state")));
                // Set the userName retrieved from the JOIN
                book.setUserName(resultSet.getString("userName"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
        }


        return books;

    }

    public void updateBookState(Book book) {
        try {
            PreparedStatement ps;
            if (book.getUserName() != null) {
                ps = connection.prepareStatement("UPDATE books SET state = ?, user_id = (SELECT id FROM users WHERE username = ?) WHERE bookId = ?");
                ps.setString(2, book.getUserName());
            } else {
                ps = connection.prepareStatement("UPDATE books SET state = ?, user_id = NULL WHERE bookId = ?");
            }
            ps.setString(1, book.getStateName());
            ps.setInt(3, book.getBookId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating the book failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating book in the database: " + e.getMessage());
            throw new RuntimeException("Failed to update the book state in the database.", e);
        }
    }


    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE bookId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting the book failed, no rows affected.");
            }
        }
    }

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, releaseYear, genre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getReleaseYear());
            statement.setString(4, book.getGenre());
            statement.executeUpdate();
        }
    }
}



