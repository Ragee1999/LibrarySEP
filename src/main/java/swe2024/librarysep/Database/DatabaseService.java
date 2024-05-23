package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.ClientObserver;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This class provides database services for interacting with book data.
 * It implements the {@link BookService} interface.
 */
public class DatabaseService implements BookService {

    private final BookDAO bookDAO;

    /**
     * Constructs a new DatabaseService with an instance of {@link BookDAO}.
     */
    public DatabaseService() {
        this.bookDAO = new BookDAO();
    }

    /**
     * Retrieves all books from the database.
     *
     * @return a list of {@link Book} objects, or null if an error occurs
     */
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

    /**
     * Updates the state of a book in the database.
     *
     * @param book the {@link Book} object with updated state
     */
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

    /**
     * Deletes a book from the database.
     *
     * @param bookId the ID of the book to be deleted
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Adds a new book to the database.
     *
     * @param book the {@link Book} object to be added
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates an existing book in the database.
     *
     * @param book the {@link Book} object with updated information
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Loads all books from the database.
     *
     * @return a list of {@link Book} objects
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public List<Book> loadBooks() throws RemoteException {
        return getAllBooks();
    }

    /**
     * Adds an observer to the service.
     * This operation is not supported by {@link DatabaseService}.
     *
     * @param observer the {@link ClientObserver} to be added
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        throw new UnsupportedOperationException("Observers are not managed by DatabaseService.");
    }

    /**
     * Removes an observer from the service.
     * This operation is not supported by {@link DatabaseService}.
     *
     * @param observer the {@link ClientObserver} to be removed
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        throw new UnsupportedOperationException("Observers are not managed by DatabaseService.");
    }
}




