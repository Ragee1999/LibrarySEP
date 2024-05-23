package swe2024.librarysep.Model;

/**
 * Represents a user with an ID, username, and password.
 * Provides constructors for user registration and user identification.
 */
public class User {
    private int userId;
    private String username;
    private String password;

    /**
     * Constructs a new User for registration purposes.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a new User with an ID for user identification.
     *
     * @param userId    the ID of the user
     * @param username  the username of the user
     * @param password  the password of the user
     */
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}



