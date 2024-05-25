package swe2024.librarysep.Test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Server.LibraryManager;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RMIServerTest {

    private static LibraryManager libraryManager;

    @BeforeAll
    public static void setUp() {
        try {
            // Start the RMI registry
            LocateRegistry.createRegistry(1099);
            // Create and bind the LibraryManager
            libraryManager = mock(LibraryManager.class);
            UnicastRemoteObject.exportObject(libraryManager, 0);
            Naming.rebind("rmi://localhost:1099/BookService", libraryManager);
            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up RMI server", e);
        }
    }

    @AfterAll
    public static void tearDown() {
        try {
            // Unbind the BookService and unexport the LibraryManager
            Naming.unbind("rmi://localhost:1099/BookService");
            UnicastRemoteObject.unexportObject(libraryManager, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to tear down RMI server", e);
        }
    }

    @Test
    public void testServerSetup() {
        try {
            // Mock the behavior of the libraryManager
            Book testBook = new Book("Test Title", "Test Author", 2024, "Test Genre");
            when(libraryManager.getAllBooks()).thenReturn(Collections.singletonList(testBook));

            // Look up the BookService in the registry
            BookService bookService = (BookService) Naming.lookup("rmi://localhost:1099/BookService");
            assertNotNull(bookService, "BookService should not be null");

            // Test retrieving all books
            List<Book> books = bookService.getAllBooks();
            assertEquals(1, books.size(), "There should be one book in the library");
            Book retrievedBook = books.get(0);
            assertEquals("Test Title", retrievedBook.getTitle());
            assertEquals("Test Author", retrievedBook.getAuthor());
            assertEquals(2024, retrievedBook.getReleaseYear());
            assertEquals("Test Genre", retrievedBook.getGenre());

            // Verify the interaction with the mock
            verify(libraryManager, times(1)).getAllBooks();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to interact with RMI server", e);
        }
    }
}



