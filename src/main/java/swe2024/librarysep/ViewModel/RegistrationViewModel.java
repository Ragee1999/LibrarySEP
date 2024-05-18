/* package swe2024.librarysep.ViewModel;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.DatabaseConnection;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class RegistrationViewModel {
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty registrationStatus = new SimpleStringProperty();

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty registrationStatusProperty() {
        return registrationStatus;
    }

    public void registerAccount() {
        String usernameValue = username.get();
        String passwordValue = password.get();

        try (Connection connection = DatabaseConnection.connect()) {
            UserService userService = new UserService(connection);
            User newUser = new User(usernameValue, passwordValue);
            userService.registerUser(newUser);
            registrationStatus.set("Success: User registered successfully!");
        } catch (SQLException e) {
            registrationStatus.set("Error: An error occurred while registering: " + e.getMessage());
        }
    }
} */

package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.User;

import java.sql.SQLException;

public class RegistrationViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty registrationStatus = new SimpleStringProperty();

    private final UserService userService;

    public RegistrationViewModel(UserService userService) {
        this.userService = userService;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty registrationStatusProperty() {
        return registrationStatus;
    }

    public void registerAccount() {
        String usernameValue = username.get();
        String passwordValue = password.get();

        try {
            User newUser = new User(usernameValue, passwordValue);
            userService.registerUser(newUser);
            registrationStatus.set("Success: User registered successfully!");
        } catch (SQLException e) {
            registrationStatus.set("Error: An error occurred while registering: " + e.getMessage());
        }
    }
}
