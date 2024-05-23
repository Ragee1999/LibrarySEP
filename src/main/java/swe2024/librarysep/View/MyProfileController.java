package swe2024.librarysep.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.ViewModel.MyProfileViewModel;

/**
 * Controller for the MyProfile view.
 * Handles the user interface interactions and binds the view to the corresponding ViewModel.
 */
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

    /**
     * Sets the ViewModel for the MyProfile view and binds the UI components to the ViewModel properties.
     *
     * @param viewModel the MyProfileViewModel to be set
     */
    public void setViewModel(MyProfileViewModel viewModel) {
        this.viewModel = viewModel;
        bindViewModel();
    }

    /**
     * Binds UI components to the ViewModel properties.
     */
    private void bindViewModel() {
        showUsername.textProperty().bind(viewModel.usernameProperty());
        bookTableViewProfile.setItems(viewModel.getUserBooks());
        viewModel.bindProfileTableColumns(titleColumn, authorColumn, releaseYearColumn, stateColumn, genreColumn);
    }

    /**
     * Handles the action when the user click on the "back to dashboard" button.
     */
    @FXML
    public void handleBackToDashboard() {
        viewModel.handleBackToDashboard();
    }
}

