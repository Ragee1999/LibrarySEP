package swe2024.librarysep.Server;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.ClientObserver;

import java.rmi.RemoteException;
import java.util.List;

/**
 * RMIBookService acts as a client-side proxy for communicating with the remote LibraryManager server.
 * This class implements the {@link BookService} interface, forwarding method calls to the {@link RMIClient},
 * which handles the actual RMI communication. It allows the client to interact with the server-side
 * LibraryManager without exposing the client to the complexities of RMI.
 */
public class RMIBookService implements BookService {
    private RMIClient client;

    /**
     * Constructs a new RMIBookService with the specified RMIClient.
     *
     * @param client the {@link RMIClient} used for RMI communication
     */
    public RMIBookService(RMIClient client) {
        this.client = client;
    }

    /**
     * Retrieves all books from the server.
     *
     * @return a list of {@link Book} objects
     */
    @Override
    public List<Book> getAllBooks() {
        return client.getAllBooks();
    }

    /**
     * Updates the state of a book on the server.
     *
     * @param book the {@link Book} object with updated state
     */
    @Override
    public void updateBookState(Book book) {
        client.updateBookState(book);
    }

    /**
     * Deletes a book on the server.
     *
     * @param bookId the ID of the book to be deleted
     */
    @Override
    public void deleteBook(int bookId) {
        client.deleteBook(bookId);
    }

    /**
     * Adds a new book to the server.
     *
     * @param book the {@link Book} object to be added
     */
    @Override
    public void addBook(Book book) {
        client.addBook(book);
    }

    /**
     * Updates an existing book on the server.
     *
     * @param book the {@link Book} object with updated information
     */
    @Override
    public void editBook(Book book) {
        client.editBook(book);
    }

    /**
     * Loads all books from the server.
     *
     * @return a list of {@link Book} objects
     */
    @Override
    public List<Book> loadBooks() {
        return client.loadBooks();
    }

    /**
     * Adds an observer to the server's observer list.
     *
     * @param observer the {@link ClientObserver} to be added
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        client.addObserver(observer);
    }

    /**
     * Removes an observer from the server's observer list.
     *
     * @param observer the {@link ClientObserver} to be removed
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        client.removeObserver(observer);
    }
}

