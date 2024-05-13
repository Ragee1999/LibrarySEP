package swe2024.librarysep.Model;

import java.io.Serializable;

public class Book implements Serializable { // We need to seralize book because of RMI
    private static final long serialVersionUID = 1L;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        // Check if userName is null, and if so, set it to an empty string instead
        this.userName = (userName != null) ? userName : "";
    }

    private Integer bookId;
    private String title;
    private String author;
    private Integer releaseYear;
    private String genre;
    private BookStates state;
    private String userName;


    public Book(Integer bookId, String title, String author, Integer releaseYear, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.state = new AvailableState();
        this.genre = genre;
    }

    // State to string for use in UI display
    public static BookStates getStateFromString(String stateString) {
        switch (stateString) {
            case "Available":
                return new AvailableState();
            case "Borrowed":
                return new BorrowedState();
            case "Reserved":
                return new ReservedState();
            default:
                throw new IllegalArgumentException("Unknown state: " + stateString);
        }
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


    public String getStateName() {
        return state.toString();
    } // for state string names

    public BookStates getState() {
        return this.state;
    }

    public void setState(BookStates state) {
        this.state = state;
    }


    public void borrow() {
        if (!(state instanceof AvailableState) && !(state instanceof ReservedState)) {
            throw new IllegalStateException("Cannot borrow a book that is not available or reserved.");
        }
        state.borrow(this);
    }

    public void returnBook() {
        if (!(state instanceof BorrowedState)) {
            throw new IllegalStateException("Cannot return a book that is not borrowed.");
        }
        state.returnBook(this);
    }

    public void reserve() {
        if (!(state instanceof AvailableState)) {
            throw new IllegalStateException("Cannot reserve a book that is not available.");
        }
        state.reserve(this);
    }

    public void cancelReservation() {
        if (!(state instanceof ReservedState)) {
            throw new IllegalStateException("Cannot cancel reservation for a book that is not reserved.");
        }
        state.cancelReservation(this);
    }
}
