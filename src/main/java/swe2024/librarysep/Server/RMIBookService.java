package swe2024.librarysep.Server;

import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import java.util.List;

public class RMIBookService implements BookService {
    private RMIClient client;

    public RMIBookService(RMIClient client) {
        this.client = client;
    }

    @Override
    public List<Book> getAllBooks() {
        return client.getAllBooks();
    }

    @Override
    public void updateBookState(Book book) {
        client.updateBookState(book);
    }
}
