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
 * LibraryManager is the server-side implementation of the BookService interface.
 * This class extends UnicastRemoteObject, making it a remote object that can be accessed via RMI.
 * It handles the actual business logic and interacts with the DatabaseService to perform CRUD operations
 * on the database. It serves requests from remote clients through RMI, allowing clients to manage
 * books in the library system.
 */

public class LibraryManager extends UnicastRemoteObject implements BookService {
    private DatabaseService databaseService;
    private List<ClientObserver> observers;

    public LibraryManager() throws RemoteException {
        super();
        this.databaseService = new DatabaseService();  // Initialize database service here
        this.observers = new ArrayList<>();
    }

    @Override
    public List<Book> getAllBooks() throws RemoteException {
        return databaseService.getAllBooks();
    }

    @Override
    public void updateBookState(Book book) throws RemoteException {
        databaseService.updateBookState(book);
        notifyObservers();
    }

    @Override
    public void deleteBook(int bookId) throws RemoteException, SQLException {
        databaseService.deleteBook(bookId);
        notifyObservers();
    }

    @Override
    public void addBook(Book book) throws RemoteException, SQLException {
        databaseService.addBook(book);
        notifyObservers();
    }

    @Override
    public void editBook(Book book) throws RemoteException, SQLException {
        databaseService.editBook(book);
        notifyObservers();
    }

    @Override
    public List<Book> loadBooks() throws RemoteException, SQLException {
        return getAllBooks();  // Simplified to call getAllBooks()
    }

    @Override
    public void addObserver(ClientObserver observer) throws RemoteException {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ClientObserver observer) throws RemoteException {
        observers.remove(observer);
    }

    private void notifyObservers() throws RemoteException {
        for (ClientObserver observer : observers) {
            observer.update();
        }
    }
}