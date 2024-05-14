package swe2024.librarysep.Model;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;


public interface BookService {

    // Retrives a list of all books from database and returns it to be useable by the book class
    List<Book> getAllBooks();
    void updateBookState(Book book); // Updates a state in the database
    void deleteBook(int bookId) throws SQLException, RemoteException;
    void addBook(Book book) throws SQLException, RemoteException;
    void updateBook(Book book) throws SQLException, RemoteException;
}


