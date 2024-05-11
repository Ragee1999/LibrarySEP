package swe2024.librarysep.ViewModel;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;

import java.util.List;

public class DashboardViewModel {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private BookService bookService;

    public DashboardViewModel(BookService bookService) {
        this.bookService = bookService;
        loadBooks();
    }

    public ObservableList<Book> getBooks() {
        return books;
    }

     private void loadBooks() { // Checks for accidentally duplicated books and updates after state change, so we don't ruin the database
        List<Book> updatedBooks = bookService.getAllBooks();
        for (Book updatedBook : updatedBooks) {
            // Check if the book already exists in the list
            boolean found = false;
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookId().equals(updatedBook.getBookId())) {
                    books.set(i, updatedBook); // Update existing book
                    found = true;
                    break;
                }
            }
            if (!found) {
                books.add(updatedBook);
            }
        }
    }

    public void updateBookState(Book book) {
        bookService.updateBookState(book);
        loadBooks();
    }


    //-----------------------------------------------------------------------
    // NEED TO ADD PRINT LOGIC TO THESE BUTTON ACTIONS ONCE WE ADD USER SETUP
    //-----------------------------------------------------------------------


    public void borrowBook(Book book) {
            book.borrow();
            updateBookState(book);
        }


    public void reserveBook(Book book) {
            book.reserve();
            updateBookState(book);
    }

    public void returnBook(Book book) {

            book.returnBook();
            updateBookState(book);
    }

    public void cancelReservation(Book book) {
            book.cancelReservation();
            updateBookState(book);
    }
}