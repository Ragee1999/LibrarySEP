package swe2024.librarysep.Model;

public class BorrowedState implements BookStates {
    private static final long serialVersionUID = 1L;
    @Override
    public void borrow(Book book) {
        throw new IllegalStateException("Book already borrowed!");
    }

    @Override
    public void returnBook(Book book) {
        book.setState(new AvailableState());
        System.out.println("Book returned!");
    }

    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book is borrowed, can't reserve!");
    }

    @Override
    public void cancelReservation(Book book) {
        throw new IllegalStateException("No reservation to cancel!");
    }

    @Override
    public String toString() {
        return "Borrowed";
    }
}
