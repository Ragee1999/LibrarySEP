package swe2024.librarysep.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.BookStateFactory;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.ViewModel.UserDashboardViewModel;

import java.rmi.RemoteException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanBookTest {

    private BookService mockBookService;
    private UserDashboardViewModel viewModel;

    @BeforeEach
    public void setUp() throws SQLException, RemoteException {
        // Mock the BookService
        mockBookService = Mockito.mock(BookService.class);
        // Initialize the ViewModel with the mocked BookService
        viewModel = new UserDashboardViewModel(mockBookService);
    }

    @Test
    public void testBorrowBookSuccess() throws SQLException, RemoteException {
        // Given
        Book book = new Book(1, "Test Title", "Test Author", 2024, "Test Genre");
        User user = new User(1, "testuser", "testpassword");

        // When
        viewModel.requestBorrowConfirmation(book, user);
        viewModel.borrowConfirmationRequestedProperty().set(true); // Simulate confirmation
        viewModel.borrowBook();

        // Then
        assertEquals(user.getUsername(), book.getUsername());
        assertEquals(user.getUserId(), book.getUserId());
        verify(mockBookService).updateBookState(book);
    }

    @Test
    public void testReturnBookSuccess() throws SQLException, RemoteException {
        // Given
        Book book = new Book(1, "Test Title", "Test Author", 2024, "Test Genre");
        book.setState(BookStateFactory.getStateFromString("Borrowed"));
        book.setUserId(1);
        book.setUsername("testuser");
        User user = new User(1, "testuser", "testpassword");

        // When
        viewModel.requestReturnConfirmation(book, user);
        viewModel.returnConfirmationRequestedProperty().set(true); // Simulate confirmation
        viewModel.returnBook();

        // Then
        assertTrue(book.getUsername() == null || book.getUsername().isEmpty(), "Username after returning the book:");
        verify(mockBookService).updateBookState(book);
    }

    @Test
    public void testReserveBookSuccess() throws SQLException, RemoteException {
        // Given
        Book book = new Book(1, "Test Title", "Test Author", 2024, "Test Genre");
        User user = new User(1, "testuser", "testpassword");

        // When
        viewModel.requestReserveConfirmation(book, user);
        viewModel.reserveConfirmationRequestedProperty().set(true); // Simulate confirmation
        viewModel.reserveBook();

        // Then
        assertEquals(user.getUsername(), book.getUsername());
        assertEquals(user.getUserId(), book.getUserId());
        verify(mockBookService).updateBookState(book);
    }

    @Test
    public void testCancelReservationSuccess() throws SQLException, RemoteException {
        // Given
        Book book = new Book(1, "Test Title", "Test Author", 2024, "Test Genre");
        book.setState(BookStateFactory.getStateFromString("Reserved"));
        book.setUserId(1);
        book.setUsername("testuser");
        User user = new User(1, "testuser", "testpassword");

        // When
        viewModel.requestCancelReservationConfirmation(book, user);
        viewModel.cancelReservationConfirmationRequestedProperty().set(true); // Simulate confirmation
        viewModel.cancelReservation();

        // Then
        assertTrue(book.getUsername() == null || book.getUsername().isEmpty(), "Username after canceling the reservation:");
        verify(mockBookService).updateBookState(book);
    }
}







