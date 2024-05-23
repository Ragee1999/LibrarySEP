package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookStateFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing {@link Book} entities in the database.
 */
public class BookDAO {

    /**
     * Retrieves all books from the database.
     *
     * @param connection the {@link Connection} object to the database
     * @return a list of {@link Book} objects
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates the state of a book in the database.
     *
     * @param connection the {@link Connection} object to the database
     * @param book       the {@link Book} object with updated state
     * @throws SQLException if a database access error occurs or if the update fails
     */
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

    /**
     * Deletes a book from the database.
     *
     * @param connection the {@link Connection} object to the database
     * @param bookId     the ID of the book to be deleted
     * @throws SQLException if a database access error occurs or if the delete fails
     */
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

    /**
     * Adds a new book to the database.
     *
     * @param connection the {@link Connection} object to the database
     * @param book       the {@link Book} object to be added
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates an existing book in the database.
     *
     * @param connection the {@link Connection} object to the database
     * @param book       the {@link Book} object with updated information
     * @throws SQLException if a database access error occurs or if the update fails
     */
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
