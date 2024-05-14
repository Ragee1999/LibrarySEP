package swe2024.librarysep.Server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import swe2024.librarysep.Database.DatabaseService;

import java.sql.SQLException;
import java.util.List;
import swe2024.librarysep.Model.Book;

public class LibraryManagerImpl extends UnicastRemoteObject implements ILibraryManager {
    private DatabaseService databaseService;

    public LibraryManagerImpl() throws RemoteException {
        super();
        this.databaseService = new DatabaseService();  // Initialize database service here
    }

    @Override
    public List<Book> getAllBooks() throws RemoteException {
        return databaseService.getAllBooks();
    }

    @Override
    public void updateBookState(Book book) throws RemoteException {
        databaseService.updateBookState(book);
    }

    @Override
    public void deleteBook(int bookId) throws RemoteException {
        try {
            databaseService.deleteBook(bookId);
        } catch (SQLException e) {
            System.err.println("SQL error during deletion: " + e.getMessage());
            throw new RemoteException("Deletion failed", e);
        }
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        try {
            databaseService.addBook(book);
        } catch (SQLException e) {
            throw new RemoteException("Database error adding book", e);
        }
    }

    @Override
    public void updateBook(Book book) throws RemoteException {
        try {
            databaseService.updateBook(book);
        } catch (SQLException e) {
            System.err.println("SQL error while updating book: " + e.getMessage());
            throw new RemoteException("Update failed", e);
        }
    }
}
