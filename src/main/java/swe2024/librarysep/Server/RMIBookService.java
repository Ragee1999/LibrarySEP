package swe2024.librarysep.Server;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;

import java.rmi.RemoteException;
import java.util.List;

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
        try {
            client.deleteBook(bookId);
        } catch (RemoteException e) {
            System.err.println("Remote exception occurred while deleting book: " + e.getMessage());
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    @Override
    public void addBook(Book book) {
        try {
            client.addBook(book);
        } catch (RemoteException e) {
            System.err.println("Remote exception occurred while adding book: " + e.getMessage());
            throw new RuntimeException("Failed to add book due to remote exception", e);
        }
    }

    @Override
    public void updateBook(Book book) {
        try {
            client.updateBook(book);
        } catch (RemoteException e) {
            System.err.println("Remote exception occurred while updating book: " + e.getMessage());
            throw new RuntimeException("Failed to update book due to remote exception", e);
        }
    }
}
