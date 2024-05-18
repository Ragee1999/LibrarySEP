package swe2024.librarysep.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;


// This interface defines the contract for the methods that can be called remotely.
// It is implemented by both the server-side LibraryManager and the client-side RMIBookService.

public interface BookService extends Remote {
    List<Book> getAllBooks() throws RemoteException, SQLException;
    void updateBookState(Book book) throws RemoteException, SQLException;
    void deleteBook(int bookId) throws RemoteException, SQLException;
    void addBook(Book book) throws RemoteException, SQLException;
    void editBook(Book book) throws RemoteException, SQLException;
    List<Book> loadBooks() throws RemoteException, SQLException;

    // Observer methods
    void addObserver(ClientObserver observer) throws RemoteException;
    void removeObserver(ClientObserver observer) throws RemoteException;
}



