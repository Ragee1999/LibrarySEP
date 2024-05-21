package swe2024.librarysep.Server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import swe2024.librarysep.Database.DatabaseService;
import swe2024.librarysep.Model.BookService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.ClientObserver;

/**
 * LibraryManager is the server-side implementation of the {@link BookService} interface.
 * This class extends {@link UnicastRemoteObject}, making it a remote object that can be accessed via RMI.
 * It handles the actual business logic and interacts with the {@link DatabaseService} to perform CRUD operations
 * on the database. It serves requests from remote clients through RMI, allowing clients to manage
 * books in the library system.
 */
public class LibraryManager extends UnicastRemoteObject implements BookService {
    private DatabaseService databaseService;
    private List<ClientObserver> observers;

    /**
     * Constructs a new LibraryManager and initializes the {@link DatabaseService} and observer list.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    public LibraryManager() throws RemoteException {
        super();
        this.databaseService = new DatabaseService();  // Initialize database service here
        this.observers = new ArrayList<>();
    }

    /**
     * Retrieves all books from the database.
     *
     * @return a list of {@link Book} objects
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public List<Book> getAllBooks() throws RemoteException {
        return databaseService.getAllBooks();
    }

    /**
     * Updates the state of a book in the database and notifies observers.
     *
     * @param book the {@link Book} object with updated state
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void updateBookState(Book book) throws RemoteException {
        databaseService.updateBookState(book);
        notifyObservers();
    }

    /**
     * Deletes a book from the database and notifies observers.
     *
     * @param bookId the ID of the book to be deleted
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    @Override
    public void deleteBook(int bookId) throws RemoteException, SQLException {
        databaseService.deleteBook(bookId);
        notifyObservers();
    }

    /**
     * Adds a new book to the database and notifies observers.
     *
     * @param book the {@link Book} object to be added
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    @Override
    public void addBook(Book book) throws RemoteException, SQLException {
        databaseService.addBook(book);
        notifyObservers();
    }

    /**
     * Updates an existing book in the database and notifies observers.
     *
     * @param book the {@link Book} object with updated information
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    @Override
    public void editBook(Book book) throws RemoteException, SQLException {
        databaseService.editBook(book);
        notifyObservers();
    }

    /**
     * Loads all books from the database.
     *
     * @return a list of {@link Book} objects
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public List<Book> loadBooks() throws RemoteException {
        return getAllBooks();  // Simplified to call getAllBooks()
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer the {@link ClientObserver} to be added
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer the {@link ClientObserver} to be removed
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of a change.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    private void notifyObservers() throws RemoteException {
        for (ClientObserver observer : observers) {
            observer.update();
        }
    }
}
