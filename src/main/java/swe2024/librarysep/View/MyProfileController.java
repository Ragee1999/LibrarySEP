package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.ViewModel.MyProfileViewModel;

public class MyProfileController {
    @FXML
    private Label showUsername;
    @FXML
    private TableView<Book> bookTableViewProfile;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> releaseYearColumn;
    @FXML
    private TableColumn<Book, String> stateColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;

    private MyProfileViewModel viewModel;

    public void setViewModel(MyProfileViewModel viewModel) {
        this.viewModel = viewModel;
        bindViewModel();
    }

    private void bindViewModel() {
        showUsername.textProperty().bind(viewModel.usernameProperty());
        bookTableViewProfile.setItems(viewModel.getUserBooks());
        viewModel.bindProfileTableColumns(titleColumn, authorColumn, releaseYearColumn, stateColumn, genreColumn);
    }

    @FXML
    public void handleBackToDashboard() {
        viewModel.handleBackToDashboard();
    }
}
