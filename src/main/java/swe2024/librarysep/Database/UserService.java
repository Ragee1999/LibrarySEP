package swe2024.librarysep.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        }
    }
}
