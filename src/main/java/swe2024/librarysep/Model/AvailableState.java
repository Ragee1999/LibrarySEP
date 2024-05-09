package swe2024.librarysep.Model;

public class AvailableState implements BookStates {
    @Override
    public void borrow(Book book) {
        book.setState(new BorrowedState());
        System.out.println("Book borrowed!");
    }

    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Can't return a book that's not borrowed!");
    }

    @Override
    public void reserve(Book book) {
        book.setState(new ReservedState());
        System.out.println("Book reserved!");
    }

    @Override
    public void cancelReservation(Book book) {
        throw new IllegalStateException("No reservation to cancel!");
    }

    @Override
    public String toString() {
        return "Available";
    }
}