package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookStateFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooks(Connection connection) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.bookId, b.title, b.author, b.releaseYear, b.state, b.genre, u.username AS username " +
                "FROM books b LEFT JOIN users u ON b.user_id = u.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("bookId"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("releaseYear"),
                        resultSet.getString("genre")
                );
                book.setState(BookStateFactory.getStateFromString(resultSet.getString("state")));
                book.setUsername(resultSet.getString("username"));
                books.add(book);
            }
        }
        return books;
    }

    public void updateBookState(Connection connection, Book book) throws SQLException {
        PreparedStatement ps;
        if (book.getUsername() != null) {
            ps = connection.prepareStatement("UPDATE books SET state = ?, user_id = (SELECT id FROM users WHERE username = ?) WHERE bookId = ?");
            ps.setString(2, book.getUsername());
        } else {
            ps = connection.prepareStatement("UPDATE books SET state = ?, user_id = NULL WHERE bookId = ?");
        }
        ps.setString(1, book.getStateName());
        ps.setInt(3, book.getBookId());
        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Updating the book failed, no rows affected.");
        }
    }

    public void deleteBook(Connection connection, int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE bookId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting the book failed, no rows affected.");
            }
        }
    }

    public void addBook(Connection connection, Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, releaseYear, genre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getReleaseYear());
            statement.setString(4, book.getGenre());
            statement.executeUpdate();
        }
    }

    public void editBook(Connection connection, Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, releaseYear = ?, genre = ? WHERE bookId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getReleaseYear());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getBookId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating the book failed, no rows affected.");
            }
        }
    }
}
