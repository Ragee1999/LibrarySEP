package swe2024.librarysep.Server;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.ClientObserver;

import java.rmi.RemoteException;
import java.util.List;

/**
 * RMIBookService acts as a client-side proxy for communicating with the remote LibraryManager server.
 * This class implements the BookService interface, forwarding method calls to the RMIClient,
 * which handles the actual RMI communication. It allows the client to interact with the server-side
 * LibraryManager without exposing the client to the complexities of RMI.
 */

public class RMIBookService implements BookService {
    private RMIClient client;

    public RMIBookService(RMIClient client) {
        this.client = client;
    }

    @Override
    public List<Book> getAllBooks() {
        return client.getAllBooks();
    }

    @Override
    public void updateBookState(Book book) {
        client.updateBookState(book);
    }

    @Override
    public void deleteBook(int bookId) {
        client.deleteBook(bookId);
    }

    @Override
    public void addBook(Book book) {
        client.addBook(book);
    }

    @Override
    public void editBook(Book book) {
        client.editBook(book);
    }

    @Override
    public List<Book> loadBooks() {
        return client.loadBooks();
    }

    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        client.addObserver(observer);
    }

    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        client.removeObserver(observer);
    }
}
