package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SceneManager;
import swe2024.librarysep.Utility.SessionManager;

/**
 * ViewModel for the login view. Manages the user input and authentication logic.
 */
public class LoginViewModel {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final UserService userService;

    /**
     * Constructs a LoginViewModel with the specified {@link UserService}.
     *
     * @param userService the {@link UserService} to use for user authentication
     */
    public LoginViewModel(UserService userService) {
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
     * Authenticates the user with the provided username and password.
     * If authentication is successful, logs in the user and navigates to the appropriate dashboard.
     *
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate() {
        User user = userService.authenticateUser(username.get(), password.get());
        if (user != null) {
            SessionManager.getInstance().loginUser(user);
            if ("Admin".equals(user.getUsername())) {
                SceneManager.showAdminDashboard();
            } else {
                SceneManager.showUserDashboard();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Navigates to the registration view.
     */
    public void showRegistration() {
        SceneManager.showRegistration();
    }
}

