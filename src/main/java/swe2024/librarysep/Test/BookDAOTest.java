package swe2024.librarysep.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swe2024.librarysep.Database.BookDAO;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookStateFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookDAOTest {

    private BookDAO bookDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        bookDAO = new BookDAO();
        connection = mock(Connection.class);
    }

    @Test
    public void testGetAllBooks() throws SQLException {
        // Given
        String sql = "SELECT b.bookId, b.title, b.author, b.releaseYear, b.state, b.genre, u.username AS username " +
                "FROM books b LEFT JOIN users u ON b.user_id = u.id";
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("bookId")).thenReturn(1);
        when(resultSet.getString("title")).thenReturn("Test Title");
        when(resultSet.getString("author")).thenReturn("Test Author");
        when(resultSet.getInt("releaseYear")).thenReturn(2024);
        when(resultSet.getString("genre")).thenReturn("Test Genre");
        when(resultSet.getString("state")).thenReturn("Available");
        when(resultSet.getString("username")).thenReturn("Test User");

        // When
        List<Book> books = bookDAO.getAllBooks(connection);

        // Then
        assertNotNull(books);
        assertEquals(1, books.size());
        Book book = books.get(0);
        assertEquals(1, book.getBookId());
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(2024, book.getReleaseYear());
        assertEquals("Test Genre", book.getGenre());
        assertEquals("Available", book.getState().toString()); // Check the state string
        assertEquals("Test User", book.getUsername());
    }

    @Test
    public void testUpdateBookState() throws SQLException {
        // Given
        Book book = new Book(1, "Test Title", "Test Author", 2024, "Test Genre");
        book.setState(BookStateFactory.getStateFromString("Borrowed"));
        book.setUsername("Test User");

        String sql = "UPDATE books SET state = ?, user_id = (SELECT id FROM users WHERE username = ?) WHERE bookId = ?";
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        // When
        bookDAO.updateBookState(connection, book);

        // Then
        verify(statement, times(1)).setString(1, "Borrowed");
        verify(statement, times(1)).setString(2, "Test User");
        verify(statement, times(1)).setInt(3, 1);
        verify(statement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteBook() throws SQLException {
        // Given
        int bookId = 1;
        String sql = "DELETE FROM books WHERE bookId = ?";
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        // When
        bookDAO.deleteBook(connection, bookId);

        // Then
        verify(statement, times(1)).setInt(1, bookId);
        verify(statement, times(1)).executeUpdate();
    }

    @Test
    public void testAddBook() throws SQLException {
        // Given
        Book book = new Book("Test Title", "Test Author", 2024, "Test Genre");
        String sql = "INSERT INTO books (title, author, releaseYear, genre) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        // When
        bookDAO.addBook(connection, book);

        // Then
        verify(statement, times(1)).setString(1, "Test Title");
        verify(statement, times(1)).setString(2, "Test Author");
        verify(statement, times(1)).setInt(3, 2024);
        verify(statement, times(1)).setString(4, "Test Genre");
        verify(statement, times(1)).executeUpdate();
    }

    @Test
    public void testEditBook() throws SQLException {
        // Given
        Book book = new Book(1, "Updated Title", "Updated Author", 2024, "Updated Genre");
        String sql = "UPDATE books SET title = ?, author = ?, releaseYear = ?, genre = ? WHERE bookId = ?";
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        // When
        bookDAO.editBook(connection, book);

        // Then
        verify(statement, times(1)).setString(1, "Updated Title");
        verify(statement, times(1)).setString(2, "Updated Author");
        verify(statement, times(1)).setInt(3, 2024);
        verify(statement, times(1)).setString(4, "Updated Genre");
        verify(statement, times(1)).setInt(5, 1);
        verify(statement, times(1)).executeUpdate();
    }
}

