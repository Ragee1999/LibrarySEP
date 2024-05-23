package swe2024.librarysep.Model;

/**
 * Represents the state of a book when it is reserved.
 */
public class ReservedState implements BookStates {
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
     * Throws an IllegalStateException because a reserved book cannot be returned.
     *
     * @param book the {@link Book} object to be returned
     */
    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Reserved book cannot be returned.");
    }

    /**
     * Throws an IllegalStateException because the book is already reserved.
     *
     * @param book the {@link Book} object to be reserved
     */
    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book is already reserved.");
    }

    /**
     * Cancels the reservation of the book, changing its state to available and prints a confirmation message.
     *
     * @param book the {@link Book} object for which the reservation is to be canceled
     */
    @Override
    public void cancelReservation(Book book) {
        book.setState(new AvailableState());
        System.out.println("Reservation cancelled!");
    }

    /**
     * Returns a string representation of the state.
     *
     * @return a string representing the state of the book
     */
    @Override
    public String toString() {
        return "Reserved";
    }
}

