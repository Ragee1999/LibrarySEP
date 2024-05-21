package swe2024.librarysep.Model;

import java.io.Serializable;

/**
 * Represents a book with its details and state.
 * The book can be borrowed, returned, reserved, or have its reservation canceled.
 * Implements Serializable for RMI purposes.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer bookId;
    private String title;
    private String author;
    private Integer releaseYear;
    private String genre;
    private BookStates state;
    private String username;  // Tracks the username of the user who borrowed/reserved the book
    private Integer userId;   // Tracks the user ID of the user who borrowed/reserved the book

    /**
     * Constructs a new Book with the specified details.
     *
     * @param bookId      the ID of the book
     * @param title       the title of the book
     * @param author      the author of the book
     * @param releaseYear the release year of the book
     * @param genre       the genre of the book
     */
    public Book(Integer bookId, String title, String author, Integer releaseYear, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.state = new AvailableState();
    }

    /**
     * Constructs a new Book without an ID for adding new books, since book IDs are auto-generated.
     *
     * @param title       the title of the book
     * @param author      the author of the book
     * @param releaseYear the release year of the book
     * @param genre       the genre of the book
     */
    public Book(String title, String author, Integer releaseYear, String genre) {
        this(null, title, author, releaseYear, genre);
    }

    /**
     * Gets the user ID associated with the book.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the book.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Gets the username associated with the book.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the book.
     * If the username is null, it sets it to an empty string instead.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = (username != null) ? username : "";
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the state name of the book as a string.
     *
     * @return the state name of the book
     */
    public String getStateName() {
        return state.toString();
    }

    /**
     * Gets the current state of the book.
     *
     * @return the current state of the book
     */
    public BookStates getState() {
        return this.state;
    }

    /**
     * Sets the state of the book.
     *
     * @param state the state to set
     */
    public void setState(BookStates state) {
        this.state = state;
    }

    /**
     * Borrows the book, changing its state to borrow.
     */
    public void borrow() {
        state.borrow(this);
    }

    /**
     * Returns the book, changing its state to available.
     */
    public void returnBook() {
        state.returnBook(this);
    }

    /**
     * Reserves the book, changing its state to reserved.
     */
    public void reserve() {
        state.reserve(this);
    }

    /**
     * Cancels the reservation of the book, changing its state to available.
     */
    public void cancelReservation() {
        state.cancelReservation(this);
    }
}