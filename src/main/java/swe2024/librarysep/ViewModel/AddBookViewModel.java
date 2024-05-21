package swe2024.librarysep.ViewModel;

import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.Book;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * ViewModel for adding a book. Interacts with the {@link BookService} to perform the add book operation.
 */
public class AddBookViewModel {
    private final BookService bookService;

    /**
     * Constructs an AddBookViewModel with the specified {@link BookService}.
     *
     * @param service the {@link BookService} to use for adding a book
     */
    public AddBookViewModel(BookService service) {
        this.bookService = service;
    }

    /**
     * Adds a new book with the specified details.
     *
     * @param title       the title of the book
     * @param author      the author of the book
     * @param releaseYear the release year of the book
     * @param genre       the genre of the book
     * @throws SQLException    if a database access error occurs
     * @throws RemoteException if a remote communication error occurs
     */
    public void addBook(String title, String author, int releaseYear, String genre) throws SQLException, RemoteException {
        Book newBook = new Book(title, author, releaseYear, genre);
        bookService.addBook(newBook);
    }
}
