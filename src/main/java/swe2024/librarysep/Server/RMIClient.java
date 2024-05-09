package swe2024.librarysep.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import swe2024.librarysep.Model.Book;

public class RMIClient {
    private ILibraryManager libraryManager;

    public RMIClient() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            libraryManager = (ILibraryManager) registry.lookup("LibraryManager");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        try {
            return libraryManager.getAllBooks();
        } catch (RemoteException e) {
            System.err.println("Error fetching books: " + e.getMessage());
            return null;
        }
    }

    public void updateBookState(Book book) {
        try {
            libraryManager.updateBookState(book);
        } catch (RemoteException e) {
            System.err.println("Error updating book state: " + e.getMessage());
        }
    }
}
