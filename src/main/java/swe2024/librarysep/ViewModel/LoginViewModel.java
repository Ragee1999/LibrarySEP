package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Database.DatabaseConnection;
import swe2024.librarysep.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public User authenticateUser() {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?"; // Consider hashing passwords
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username.get());
                ps.setString(2, password.get());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Assuming the first column in your user table is the user id
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
        }
        return null; // Return null if authentication fails
    }
}
