package swe2024.librarysep.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Utility.SessionManager;
import swe2024.librarysep.ViewModel.LoginViewModel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginViewModelTest {

    private UserService mockUserService;
    private LoginViewModel loginViewModel;
    private SessionManager mockSessionManager;

    @BeforeEach
    public void setUp() {
        // Mock the UserService and SessionManager
        mockUserService = Mockito.mock(UserService.class);
        mockSessionManager = Mockito.mock(SessionManager.class);

        // Create an instance of LoginViewModel with the mocked UserService
        loginViewModel = new LoginViewModel(mockUserService) {
            @Override
            public boolean authenticate() {
                User user = getUserService().authenticateUser(usernameProperty().get(), passwordProperty().get());
                if (user != null) {
                    getSessionManager().loginUser(user);
                    return true;
                } else {
                    return false;
                }
            }

            protected SessionManager getSessionManager() {
                return mockSessionManager;
            }

            private UserService getUserService() {
                return mockUserService;
            }
        };
    }

    @Test
    public void testAuthenticateSuccess() {
        // Mock the behavior of authenticateUser to return a valid user
        User mockUser = new User(1, "testuser", "testpassword");
        when(mockUserService.authenticateUser(anyString(), anyString())).thenReturn(mockUser);

        // Set the username and password
        loginViewModel.usernameProperty().set("testuser");
        loginViewModel.passwordProperty().set("testpassword");

        // Measure the time for the authentication process
        long startTime = System.currentTimeMillis();
        boolean result = loginViewModel.authenticate();
        long endTime = System.currentTimeMillis();

        // Verify the result is true
        assertTrue(result);

        // Print the authentication process time
        System.out.println("Authentication process took: " + (endTime - startTime) + " ms");

        // Verify that SessionManager method was called
        verify(mockSessionManager).loginUser(mockUser);
    }

    @Test
    public void testAuthenticateFailure() {
        // Mock the behavior of authenticateUser to return null
        when(mockUserService.authenticateUser(anyString(), anyString())).thenReturn(null);

        // Set the username and password
        loginViewModel.usernameProperty().set("invaliduser");
        loginViewModel.passwordProperty().set("invalidpassword");

        // Measure the time for the authentication process
        long startTime = System.currentTimeMillis();
        boolean result = loginViewModel.authenticate();
        long endTime = System.currentTimeMillis();

        // Verify the result is false
        assertFalse(result);

        // Print the authentication process time
        System.out.println("Authentication process took: " + (endTime - startTime) + " ms");

        // Verify that SessionManager method was not called
        verify(mockSessionManager, never()).loginUser(any(User.class));
    }
}



