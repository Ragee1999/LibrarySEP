package swe2024.librarysep.Model;

import java.io.Serializable;

/**
 * Represents the state of a book when it is borrowed.
 */
public class BorrowedState implements BookStates, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Throws an IllegalStateException because a borrowed book cannot be borrowed again.
     *
     * @param book the {@link Book} object to be borrowed
     */
    @Override
    public void borrow(Book book) {
        throw new IllegalStateException("Book is already borrowed.");
    }

    /**
     * Changes the state of the book to available and prints a confirmation message.
     *
     * @param book the {@link Book} object to be returned
     */
    @Override
    public void returnBook(Book book) {
        book.setState(new AvailableState());
        System.out.println("Book returned!");
    }

    /**
     * Throws an IllegalStateException because a borrowed book cannot be reserved.
     *
     * @param book the {@link Book} object to be reserved
     */
    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book is borrowed and cannot be reserved.");
    }

    /**
     * Throws an IllegalStateException because there is no reservation to cancel for a borrowed book.
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
        return "Borrowed";
    }
}



