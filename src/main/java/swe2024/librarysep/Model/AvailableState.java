package swe2024.librarysep.Model;

import java.io.Serializable;

/**
 * Represents the state of a book when it is available for borrowing.
 */
public class AvailableState implements BookStates, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Changes the state of the book to borrowed and prints a confirmation message.
     *
     * @param book the {@link Book} object to be borrowed
     */
    @Override
    public void borrow(Book book) {
        book.setState(new BorrowedState());
        System.out.println("Book borrowed!");
    }

    /**
     * Throws an IllegalStateException because a book that is not borrowed cannot be returned.
     *
     * @param book the {@link Book} object to be returned
     */
    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Can't return a book that's not borrowed!");
    }

    /**
     * Changes the state of the book to reserved and prints a confirmation message.
     *
     * @param book the {@link Book} object to be reserved
     */
    @Override
    public void reserve(Book book) {
        book.setState(new ReservedState());
        System.out.println("Book reserved!");
    }

    /**
     * Throws an IllegalStateException because there is no reservation to cancel.
     *
     * @param book the {@link Book} object for which the reservation is to be canceled
     */
    @Override
    public void cancelReservation(Book book) {
        throw new IllegalStateException("No reservation to cancel!");
    }

    /**
     * Returns a string representation of the state.
     *
     * @return a string representing the state of the book
     */
    @Override
    public String toString() {
        return "Available";
    }
}
