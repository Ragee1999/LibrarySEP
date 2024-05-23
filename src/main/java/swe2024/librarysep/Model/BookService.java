package swe2024.librarysep.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/**
 * Defines the contract for methods that can be called remotely in an RMI system.
 * It is implemented by both the server-side LibraryManager and the client-side RMIBookService.
 */
public interface BookService extends Remote {

    /**
     * Retrieves all books from the database.
     *
     * @return a list of {@link Book} objects
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    List<Book> getAllBooks() throws RemoteException, SQLException;

    /**
     * Updates the state of a book in the database.
     *
     * @param book the {@link Book} object with updated state
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    void updateBookState(Book book) throws RemoteException, SQLException;

    /**
     * Deletes a book from the database.
     *
     * @param bookId the ID of the book to be deleted
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    void deleteBook(int bookId) throws RemoteException, SQLException;

    /**
     * Adds a new book to the database.
     *
     * @param book the {@link Book} object to be added
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    void addBook(Book book) throws RemoteException, SQLException;

    /**
     * Updates an existing book in the database.
     *
     * @param book the {@link Book} object with updated information
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    void editBook(Book book) throws RemoteException, SQLException;

    /**
     * Loads all books from the database.
     *
     * @return a list of {@link Book} objects
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    List<Book> loadBooks() throws RemoteException, SQLException;

    /**
     * Adds an observer to the service.
     *
     * @param observer the {@link ClientObserver} to be added
     * @throws RemoteException if a remote communication error occurs
     */
    void addObserver(ClientObserver observer) throws RemoteException;

    /**
     * Removes an observer from the service.
     *
     * @param observer the {@link ClientObserver} to be removed
     * @throws RemoteException if a remote communication error occurs
     */
    void removeObserver(ClientObserver observer) throws RemoteException;
}



