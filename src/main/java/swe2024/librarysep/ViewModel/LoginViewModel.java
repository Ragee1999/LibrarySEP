package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.DatabaseConnection;
import swe2024.librarysep.Database.UserService;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginViewModel {
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    public StringProperty usernameProperty() {
        return username;
    }
    public StringProperty passwordProperty() {
        return password;
    }

    public boolean authenticateUser() {
        try (Connection connection = DatabaseConnection.connect()) {
            UserService userService = new UserService(connection);
            return userService.authenticateUser(username.get(), password.get());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}