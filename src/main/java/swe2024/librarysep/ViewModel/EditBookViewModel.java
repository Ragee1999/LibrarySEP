package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * ViewModel for editing a book. Interacts with the {@link BookService} to perform the edit book operation.
 * Manages the properties for the book details to be edited.
 */
public class EditBookViewModel {

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty author = new SimpleStringProperty();
    private final StringProperty releaseYear = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();

    private Book currentBook;
    private final BookService bookService;

    /**
     * Constructs an EditBookViewModel with the specified {@link BookService}.
     *
     * @param service the {@link BookService} to use for editing the book
     */
    public EditBookViewModel(BookService service) {
        this.bookService = service;
    }

    /**
     * Sets the current book to be edited and initializes the properties with the book's details.
     *
     * @param book the book to be edited
     */
    public void setBook(Book book) {
        this.currentBook = book;
        title.set(book.getTitle());
        author.set(book.getAuthor());
        releaseYear.set(book.getReleaseYear().toString());
        genre.set(book.getGenre());
    }

    // Getters for the properties
    /**
     * Returns the title property.
     *
     * @return the title property
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Returns the author property.
     *
     * @return the author property
     */
    public StringProperty authorProperty() {
        return author;
    }

    /**
     * Returns the release year property.
     *
     * @return the release year property
     */
    public StringProperty releaseYearProperty() {
        return releaseYear;
    }

    /**
     * Returns the genre property.
     *
     * @return the genre property
     */
    public StringProperty genreProperty() {
        return genre;
    }

    /**
     * Edits the current book with the details from the properties.
     *
     * @throws RemoteException if a remote communication error occurs
     * @throws SQLException    if a database access error occurs
     */
    public void editBook() throws RemoteException, SQLException {
        validateInputs();
        currentBook.setTitle(title.get());
        currentBook.setAuthor(author.get());
        currentBook.setReleaseYear(Integer.parseInt(releaseYear.get()));
        currentBook.setGenre(genre.get());
        bookService.editBook(currentBook);
    }

    /**
     * Validates the input fields to ensure they are correctly filled out.
     *
     * @throws IllegalArgumentException if any field is empty
     * @throws NumberFormatException    if the release year is not a valid number
     */
    private void validateInputs() throws IllegalArgumentException {
        if (title.get().isEmpty() || author.get().isEmpty() || genre.get().isEmpty() || releaseYear.get().isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled out");
        }
        try {
            Integer.parseInt(releaseYear.get());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Release Year must be a valid number");
        }
    }
}
