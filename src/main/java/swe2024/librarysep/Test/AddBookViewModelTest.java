package swe2024.librarysep.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.ViewModel.AddBookViewModel;

import java.rmi.RemoteException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AddBookViewModelTest {

    private BookService bookService;
    private AddBookViewModel viewModel;

    @BeforeEach
    public void setUp() {
        bookService = mock(BookService.class);
        viewModel = new AddBookViewModel(bookService);
    }

    @Test
    public void testAddBookSuccess() throws SQLException, RemoteException {
        // Given
        String title = "Test Title";
        String author = "Test Author";
        int releaseYear = 2024;
        String genre = "Test Genre";

        // When
        viewModel.addBook(title, author, releaseYear, genre);

        // Then
        verify(bookService, times(1)).addBook(any());
    }

    @Test
    public void testAddBookThrowsSQLException() throws SQLException, RemoteException {
        // Given
        String title = "Test Title";
        String author = "Test Author";
        int releaseYear = 2024;
        String genre = "Test Genre";
        doThrow(new SQLException("Database error")).when(bookService).addBook(any());

        // When / Then
        assertThrows(SQLException.class, () -> {
            viewModel.addBook(title, author, releaseYear, genre);
        });
    }

    @Test
    public void testAddBookThrowsRemoteException() throws SQLException, RemoteException {
        // Given
        String title = "Test Title";
        String author = "Test Author";
        int releaseYear = 2024;
        String genre = "Test Genre";
        doThrow(new RemoteException("Remote error")).when(bookService).addBook(any());

        // When / Then
        assertThrows(RemoteException.class, () -> {
            viewModel.addBook(title, author, releaseYear, genre);
        });
    }
}