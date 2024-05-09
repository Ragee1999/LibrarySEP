package swe2024.librarysep.Model;

import java.io.Serializable;

public interface BookStates extends Serializable {

    void borrow(Book book);
    void returnBook(Book book);
    void reserve(Book book);
    void cancelReservation(Book book);
}
