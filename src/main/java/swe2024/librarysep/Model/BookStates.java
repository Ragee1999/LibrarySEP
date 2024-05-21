package swe2024.librarysep.Model;

import java.io.Serializable;

/**
 * Represents the state of a book. Defines the actions that can be performed on the book.
 * Implementations of this interface should define the behavior for each state-specific action.
 */
public interface BookStates extends Serializable {

    /**
     * Borrows the book, changing its state.
     *
     * @param book the {@link Book} object to be borrowed
     */
    void borrow(Book book);

    /**
     * Returns the book, changing its state.
     *
     * @param book the {@link Book} object to be returned
     */
    void returnBook(Book book);

    /**
     * Reserves the book, changing its state.
     *
     * @param book the {@link Book} object to be reserved
     */
    void reserve(Book book);

    /**
     * Cancels the reservation of the book, changing its state.
     *
     * @param book the {@link Book} object for which the reservation is to be canceled
     */
    void cancelReservation(Book book);
}
