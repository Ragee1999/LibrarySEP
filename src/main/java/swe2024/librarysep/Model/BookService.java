package swe2024.librarysep.Model;

import java.util.List;


public interface BookService {

    // Retrives a list of all books from database and returns it to be useable by the book class
    List<Book> getAllBooks();

    // Updates a state in the database
    void updateBookState(Book book);
}

