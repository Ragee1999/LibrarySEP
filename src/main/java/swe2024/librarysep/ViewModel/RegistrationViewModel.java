package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.User;

import java.sql.SQLException;

/**
 * ViewModel for the user registration view.
 * Manages user input and registration logic.
 */
public class RegistrationViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty registrationStatus = new SimpleStringProperty();

    private final UserService userService;

    /**
     * Constructs a RegistrationViewModel with the specified UserService.
     *
     * @param userService the UserService to use for user registration
     */
    public RegistrationViewModel(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns the username property.
     *
     * @return the username property
     */
    public StringProperty usernameProperty() {
        return username;
    }

    /**
     * Returns the password property.
     *
     * @return the password property
     */
    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * Returns the registration status property.
     *
     * @return the registration status property
     */
    public StringProperty registrationStatusProperty() {
        return registrationStatus;
    }

    /**
     * Registers a new user account with the provided username and password.
     */
    public void registerAccount() {
        String usernameValue = username.get();
        String passwordValue = password.get();

        try {
            User newUser = new User(usernameValue, passwordValue);
            userService.registerUser(newUser);
            registrationStatus.set("Success: User registered successfully!");
        } catch (SQLException e) {
            registrationStatus.set("Error: An error occurred while registering: Username already exists");
        }
    }
}
