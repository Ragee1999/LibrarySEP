package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Database.UserService;

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

    public User authenticate() {
        User user = userService.authenticateUser(username.get(), password.get());
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
}
