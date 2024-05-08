package swe2024.librarysep.Database;

import swe2024.librarysep.Model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final DatabaseConnection databaseConnection;

    public DatabaseService() {
        this.databaseConnection = new DatabaseConnection();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT bookId, title, author, releaseYear FROM books";

        try (Connection connection = databaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int bookId = resultSet.getInt("bookId");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int releaseYear = resultSet.getInt("releaseYear");

                Book book = new Book(bookId, title, author, releaseYear);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
        }

        return books;
    }
}