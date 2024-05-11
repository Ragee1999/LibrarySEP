package swe2024.librarysep.Database;


import swe2024.librarysep.Model.Author;
import swe2024.librarysep.Model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO{

    private static BookDAOImpl instance;

    private BookDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver()); //need a JDBC for the driver!!!
    }

    public static synchronized BookDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new BookDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=jdbc", "postgres", "admin");
    }


    @Override
    public Book create(String isbn, String title, int releaseYear, Author author) throws SQLException {
        return null;
    }

    @Override
    public Book readByISBN(String isbn) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Book JOIN Author ON author_id = id WHERE isbn = ?");
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                int year = resultSet.getInt("yearOfPublishing");
                int authorId = resultSet.getInt("author_id");
                String authorName = resultSet.getString("name");
                Author author = new Author(authorId, authorName);
                Book book = createBook(resultSet);
                return book;
            } else {
                return null;
            }
        }
    }

    @Override
    public List<Book> readByTitle(String searchString) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Book JOIN Author ON author_id = id WHERE title LIKE ?");
            statement.setString(1, "%" + searchString + "%");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Book> result = new ArrayList<>();
            while (resultSet.next()) {
                Book book = createBook(resultSet);
                result.add(book);
            }
            return result;
        }
    }

    public static Book createBook(ResultSet resultSet) throws SQLException {
        String isbn = resultSet.getString("isbn");
        String title = resultSet.getString("title");
        int year = resultSet.getInt("yearOfPublishing");
        int authorId = resultSet.getInt("author_id");
        String authorName = resultSet.getString("name");
        Author author = new Author(authorId, authorName);
        return new Book(isbn, title, year, author);
    }

    @Override
    public void update(Book book) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Book SET title = ?, yearOfPublishing = ?, author_id = ? WHERE isbn = ?");
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getReleaseYear());
            statement.setInt(3, book.getAuthor().getid());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Book book) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Book WHERE isbn = ?");
            statement.setString(1, book.getIsbn());
            statement.executeUpdate();
        }
    }





}
