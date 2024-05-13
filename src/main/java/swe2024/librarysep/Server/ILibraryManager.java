package swe2024.librarysep.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import swe2024.librarysep.Model.Book;

public interface ILibraryManager extends Remote {
    List<Book> getAllBooks() throws RemoteException;
    void updateBookState(Book book) throws RemoteException;
    void deleteBook(int bookId) throws RemoteException;
    void addBook(Book book) throws RemoteException;
}
