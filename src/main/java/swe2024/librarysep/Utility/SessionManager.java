package swe2024.librarysep.Utility;


import swe2024.librarysep.Model.User;

public class SessionManager {
    private static User currentUser;

    public static void loginUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}