package swe2024.librarysep.Model;

public class SessionManager {
    private static User currentUser;

    public static void loginUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}