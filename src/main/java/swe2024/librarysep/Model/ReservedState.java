package swe2024.librarysep.Model;

public class ReservedState implements BookStates {
    private static final long serialVersionUID = 1L;

    @Override
    public void borrow(Book book) {
        book.setState(new BorrowedState());
        System.out.println("Book borrowed!");
    }

    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Reserved book cannot be returned.");
    }

    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book is already reserved.");
    }

    @Override
    public void cancelReservation(Book book) {
        book.setState(new AvailableState());
        System.out.println("Reservation cancelled!");
    }

    @Override
    public String toString() {
        return "Reserved";
    }
}
    /* public void borrow(Book book) {
        book.setState(new BorrowedState());
        System.out.println("Book borrowed successfully!");
    }

    @Override
    public void returnBook(Book book) {
        throw new IllegalStateException("Book isn't borrowed!");
    }

    @Override
    public void reserve(Book book) {
        throw new IllegalStateException("Book already reserved!");
    }

    @Override
    public void cancelReservation(Book book) {
        book.setState(new AvailableState());
        System.out.println("Reservation cancelled!");
    }

    @Override
    public String toString() {
        return "Reserved";
    }
} */
