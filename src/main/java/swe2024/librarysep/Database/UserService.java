package swe2024.librarysep.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import swe2024.librarysep.Model.User;

/**
 * Provides services for user registration and authentication.
 */
public class UserService {
    private final Connection connection;

    /**
     * Constructs a UserService with the provided database connection.
     *
     * @param connection the {@link Connection} object to the database
     */
    public UserService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Registers a new user in the database.
     *
     * @param user the {@link User} object to be registered
     * @throws SQLException if a database access error occurs
     */
    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("An error occurred while registering: " + e.getMessage(), e);
        }
    }

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the authenticated {@link User} object, or null if authentication fails
     */
    public User authenticateUser(String username, String password) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        String userUsername = rs.getString("username");
                        String userPassword = rs.getString("password");
                        return new User(userId, userUsername, userPassword);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                DatabaseConnection.returnConnection(connection);
            }
        }
        return null; // Return null if authentication fails
    }
}