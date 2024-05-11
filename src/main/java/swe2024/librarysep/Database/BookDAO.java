package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Author;
import swe2024.librarysep.Model.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
    Book create (String isbn, String title, int releaseYear, Author author) throws SQLException;
    Book readByISBN(String isbn) throws SQLException;
    List<Book> readByTitle(String searchString) throws SQLException;

    void update (Book book) throws SQLException;
    void delete (Book book) throws SQLException;

}
