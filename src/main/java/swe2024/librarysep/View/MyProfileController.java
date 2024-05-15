package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.ViewModel.MyProfileViewModel;

public class MyProfileController {
    @FXML
    private Label showUsername;
    @FXML
    private TableView<Book> bookTableViewProfile;

    private MyProfileViewModel viewModel;

    public void setViewModel(MyProfileViewModel viewModel) {
        this.viewModel = viewModel;
        bindViewModel();
    }

    private void bindViewModel() {
        showUsername.textProperty().bind(viewModel.usernameProperty());
        // Add binding for bookTableViewProfile if needed
    }
}