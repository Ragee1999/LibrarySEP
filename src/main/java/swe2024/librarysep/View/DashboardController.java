package swe2024.librarysep.View;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import swe2024.librarysep.Database.DatabaseService;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.ViewModel.DashboardViewModel;

public class DashboardController {


  @FXML
  private Button bookBorrow;

  @FXML
  private Button bookReserve;

  @FXML
  private Button bookReturn;

  @FXML
  private TableView<Book> bookTableView;

  @FXML
  private TableColumn<Book, String> titleColumn;
  @FXML
  private TableColumn<Book, String> authorColumn;
  @FXML
  private TableColumn<Book, Integer> releaseYearColumn;
  @FXML
  private TableColumn<Book, Integer> idColumn;



  private final DashboardViewModel viewModel;

  public DashboardController() {
    this.viewModel = new DashboardViewModel(new DatabaseService());
  }

  @FXML
  public void initialize() {
    // Initialize TableView and propertybind
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
    idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

    // Fills TableView with data from ViewModel
    bookTableView.setItems(viewModel.getAllBooks());
  }
}