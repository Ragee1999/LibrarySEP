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



