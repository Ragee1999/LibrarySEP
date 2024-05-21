package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.Utility.SessionManager;

public class LoginViewModel {
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private UserService userService;

    public LoginViewModel(UserService userService) {
        this.userService = userService;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public boolean authenticate() {
        User user = userService.authenticateUser(username.get(), password.get());
        if (user != null) {
            SessionManager.getInstance().loginUser(user);
            if (user.getUsername().equals("Admin")) {
                SceneManager.showAdminDashboard();
            } else {
                SceneManager.showUserDashboard();
            }
            return true;
        } else {
            return false;
        }
    }

    public void showRegistration() {
        SceneManager.showRegistration();
    }

}
