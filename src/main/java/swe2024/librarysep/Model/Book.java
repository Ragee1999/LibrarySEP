package swe2024.librarysep.Model;

import java.io.Serializable;

public class Book implements Serializable { // We need to seralize book because of RMI
    private static final long serialVersionUID = 1L;

    private Integer bookId;
    private String title;
    private String author;
    private Integer releaseYear;
    private BookStates state;


    public Book(Integer bookId, String title, String author, Integer releaseYear) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.state = new AvailableState();
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

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
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

    public String getStateName() {
        return state.toString();
    }
}
