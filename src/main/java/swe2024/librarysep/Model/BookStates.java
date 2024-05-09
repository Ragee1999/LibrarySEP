package swe2024.librarysep.Model;

public interface BookStates {
    void borrow(Book book);
    void returnBook(Book book);
    void reserve(Book book);
    void cancelReservation(Book book);
}
