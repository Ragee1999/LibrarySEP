package swe2024.librarysep.ViewModel;

import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.Book;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class AddBookViewModel {
    private final BookService bookService;

    public AddBookViewModel(BookService service) {
        this.bookService = service;
    }

    public void addBook(String title, String author, int releaseYear, String genre) throws SQLException, RemoteException {
        Book newBook = new Book(title, author, releaseYear, genre);
        bookService.addBook(newBook);
    }
}
