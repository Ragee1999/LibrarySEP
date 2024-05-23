package swe2024.librarysep.Utility;

import swe2024.librarysep.Model.User;

/**
 * Thread-safe, synchronized SessionManager for use while logging in or getting the current user.
 */
public class SessionManager {
    private static volatile SessionManager instance;
    private User currentUser;

    /**
     * Private constructor to prevent instantiation.
     */
    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of the SessionManager.
     * Uses double-checked locking for thread-safe lazy initialization.
     *
     * @return the singleton instance of the SessionManager
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Logs in a user by setting the current user.
     * This method is synchronized to ensure thread safety.
     *
     * @param user the user to log in
     */
    public synchronized void loginUser(User user) {
        this.currentUser = user;
        System.out.println("Synchronized: User " + user.getUsername() + " logged in.");
    }

    /**
     * Gets the current user.
     * This method is synchronized to ensure thread safety.
     *
     * @return the current user, or null if no user is logged in
     */
    public synchronized User getCurrentUser() {
        System.out.println("getCurrentUser() called. Current user: " + (currentUser != null ? currentUser.getUsername() : "null"));
        return currentUser;
    }
}
