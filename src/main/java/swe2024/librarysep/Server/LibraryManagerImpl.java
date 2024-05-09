package swe2024.librarysep.Server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import swe2024.librarysep.Database.DatabaseService;
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
}
