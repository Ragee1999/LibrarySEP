package swe2024.librarysep.Database;

import java.sql.*;

import swe2024.librarysep.Model.User;

public class UserService {
    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("An error occurred while registering: " + e.getMessage());
        }
    }

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