package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class EditBookViewModel {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty author = new SimpleStringProperty();
    private final StringProperty releaseYear = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();

    private Book currentBook;
    private BookService bookService;

    public EditBookViewModel(BookService service) {
        this.bookService = service;
    }

    public void setBook(Book book) {
        this.currentBook = book;
        title.set(book.getTitle());
        author.set(book.getAuthor());
        releaseYear.set(book.getReleaseYear().toString());
        genre.set(book.getGenre());
    }

    // Getters for the properties
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty releaseYearProperty() { return releaseYear; }
    public StringProperty genreProperty() { return genre; }


    public void editBook() throws RemoteException, SQLException {
        currentBook.setTitle(title.get());
        currentBook.setAuthor(author.get());
        currentBook.setReleaseYear(Integer.parseInt(releaseYear.get()));
        currentBook.setGenre(genre.get());
        bookService.editBook(currentBook);
    }
}