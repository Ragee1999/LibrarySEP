/* package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.BookStateFactory;
import swe2024.librarysep.Model.ClientObserver;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// This class provides database services for interacting with book data.
// It implements the BookService interface.
// CRUD Operations and business logic is combined, as there are no need for DAO objects in this small project.
public class DatabaseService implements BookService {



    // Retrieves a list of all books from the database making it into java objects in this case Books.
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
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
                book.setState(BookStateFactory.getStateFromString(resultSet.getString("state")));
                // Set the userName retrieved from the JOIN
                book.setUsername(resultSet.getString("userName"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
        return books;
    }

    // Updates the book state in the database.
    public void updateBookState(Book book) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
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
        } catch (SQLException e) {
            System.out.println("Error updating book in the database: " + e.getMessage());
            throw new RuntimeException("Failed to update the book state in the database.", e);
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    // Deletes a book in the database
    public void deleteBook(int bookId) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            String sql = "DELETE FROM books WHERE bookId = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, bookId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Deleting the book failed, no rows affected.");
                }
            }
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    // Adds a book into the database
    public void addBook(Book book) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            String sql = "INSERT INTO books (title, author, releaseYear, genre) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getAuthor());
                statement.setInt(3, book.getReleaseYear());
                statement.setString(4, book.getGenre());
                statement.executeUpdate();
            }
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    // Updates one book in the database
    public void editBook(Book book) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
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
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    @Override
    public List<Book> loadBooks() throws RemoteException {
        return getAllBooks(); // Calls the getAllBooks method to get the list of all books
    }

    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        throw new UnsupportedOperationException("Observers are not managed by DatabaseService.");
    }

    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        throw new UnsupportedOperationException("Observers are not managed by DatabaseService.");
    }
} */

package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.ClientObserver;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// This class provides database services for interacting with book data.
// It implements the BookService interface.
public class DatabaseService implements BookService {

    private final BookDAO bookDAO;

    public DatabaseService() {
        this.bookDAO = new BookDAO();
    }

    @Override
    public List<Book> getAllBooks() {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            return bookDAO.getAllBooks(connection);
        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    @Override
    public void updateBookState(Book book) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            bookDAO.updateBookState(connection, book);
        } catch (SQLException e) {
            System.out.println("Error updating book in the database: " + e.getMessage());
            throw new RuntimeException("Failed to update the book state in the database.", e);
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    @Override
    public void deleteBook(int bookId) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            bookDAO.deleteBook(connection, bookId);
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    @Override
    public void addBook(Book book) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            bookDAO.addBook(connection, book);
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    @Override
    public void editBook(Book book) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            bookDAO.editBook(connection, book);
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
    }

    @Override
    public List<Book> loadBooks() throws RemoteException {
        return getAllBooks();
    }

    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        throw new UnsupportedOperationException("Observers are not managed by DatabaseService.");
    }

    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        throw new UnsupportedOperationException("Observers are not managed by DatabaseService.");
    }
}



