package swe2024.librarysep.Model;

import java.io.Serializable;

public class Book implements Serializable { // We need to seralize book because of RMI
    private static final long serialVersionUID = 1L;


    private Integer bookId;
    private String title;
    private String author;
    private Integer releaseYear;
    private String genre;
    private BookStates state;
    private String username;  // Tracks the username of the user who borrowed/reserved the book
    private Integer userId;   // Tracks the user ID of the user who borrowed/reserved the book

    public Book(Integer bookId, String title, String author, Integer releaseYear, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.state = new AvailableState();
        this.genre = genre;
    }

    // Constructor without bookId for adding new books, since books added are using Serial primal key
    public Book(String title, String author, Integer releaseYear, String genre) {
        this(null, title, author, releaseYear, genre);
    }


    /**
     * userId and username attributes are in the book class mainly to track the user during loan actions.
     * userId's are stored in the book table in the database and linked to the users table.
     * The username is  used to set the user_id in the book table during loan actions. Check database service for the relevant crud operations
     */

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        // Check if username is null, and if so, set it to an empty string instead
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

    // for state string names
    public String getStateName() {
        return state.toString();
    }

    public BookStates getState() {
        return this.state;
    }

    public void setState(BookStates state) {
        this.state = state;
    }

    public void borrow() {
        state.borrow(this);
    }

    public void returnBook() {
        state.returnBook(this);
    }

    public void reserve() {
        state.reserve(this);
    }

    public void cancelReservation() {
        state.cancelReservation(this);
    }
}
