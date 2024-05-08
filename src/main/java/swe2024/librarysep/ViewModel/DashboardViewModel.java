package swe2024.librarysep.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe2024.librarysep.Database.DatabaseService;
import swe2024.librarysep.Model.Book;

import java.util.List;

public class DashboardViewModel {
    private final DatabaseService databaseService;

    public DashboardViewModel(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public ObservableList<Book> getAllBooks() {
        List<Book> books = databaseService.getAllBooks();
        return FXCollections.observableArrayList(books);
    }
}