package swe2024.librarysep.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SessionManager;

import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    public void setUp() {
        sessionManager = SessionManager.getInstance();
    }

    @Test
    public void testLoginUser() {
        // Given
        User user = new User(1, "testuser", "testpassword");

        // When
        sessionManager.loginUser(user);

        // Then
        assertEquals(user, sessionManager.getCurrentUser());
    }
}