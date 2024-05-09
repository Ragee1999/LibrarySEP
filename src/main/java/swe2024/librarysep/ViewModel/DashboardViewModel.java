package swe2024.librarysep.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.Book;
import java.util.List;

public class DashboardViewModel {

    // This is the entire list of books in the UI
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    private final BookService bookService;

    // this constructor initalizes the list of books
    public DashboardViewModel(BookService bookService) {
        this.bookService = bookService;
        loadBooks();
    }

    // Getter for the list of books
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


    // Updates the state of a book in the database and refreshes the books in the UI.
    public void updateBookState(Book book){
        bookService.updateBookState(book);
        loadBooks();
    }

}