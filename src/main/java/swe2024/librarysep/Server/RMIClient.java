package swe2024.librarysep.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.ClientObserver;

/**
 * RMIClient handles the client-side communication with the RMI server.
 * It connects to the RMI registry, looks up the {@link BookService}, and provides methods
 * to interact with the server-side {@link BookService} for various operations on books.
 */
public class RMIClient {
    private BookService bookService;

    /**
     * Constructs an RMIClient and establishes a connection to the RMI registry.
     * It looks up the {@link BookService} in the registry and initializes the bookService field.
     */
    public RMIClient() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            bookService = (BookService) registry.lookup("BookService");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all books from the server.
     *
     * @return a list of {@link Book} objects or null if an error occurs
     */
    public List<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (RemoteException | SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates the state of a book on the server.
     *
     * @param book the {@link Book} to update
     */
    public void updateBookState(Book book) {
        try {
            bookService.updateBookState(book);
        } catch (RemoteException | SQLException e) {
            System.err.println("Error updating book state: " + e.getMessage());
        }
    }

    /**
     * Deletes a book on the server.
     *
     * @param bookId the ID of the {@link Book} to delete
     */
    public void deleteBook(int bookId) {
        try {
            bookService.deleteBook(bookId);
        } catch (RemoteException | SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }

    /**
     * Adds a new book on the server.
     *
     * @param book the {@link Book} to add
     */
    public void addBook(Book book) {
        try {
            bookService.addBook(book);
        } catch (RemoteException | SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    /**
     * Updates an existing book on the server.
     *
     * @param book the {@link Book} to update
     */
    public void editBook(Book book) {
        try {
            bookService.editBook(book);
        } catch (RemoteException | SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }

    /**
     * Loads all books from the server.
     *
     * @return a list of {@link Book} objects or null if an error occurs
     */
    public List<Book> loadBooks() {
        try {
            return bookService.loadBooks();
        } catch (RemoteException | SQLException e) {
            System.err.println("Error loading books: " + e.getMessage());
            return null;
        }
    }

    /**
     * Adds an observer to the server's observer list.
     *
     * @param observer the {@link ClientObserver} to be added
     * @throws RemoteException if a remote communication error occurs
     */
    public void addObserver(ClientObserver observer) throws RemoteException {
        bookService.addObserver(observer);
    }

    /**
     * Removes an observer from the server's observer list.
     *
     * @param observer the {@link ClientObserver} to be removed
     * @throws RemoteException if a remote communication error occurs
     */
    public void removeObserver(ClientObserver observer) throws RemoteException {
        bookService.removeObserver(observer);
    }
}
