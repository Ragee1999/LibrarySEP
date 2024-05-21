package swe2024.librarysep.Utility;

import swe2024.librarysep.Model.User;


// Thread safe synchronized SessionManager for use while logging in or getting a currentUser
public class SessionManager {
    private static volatile SessionManager instance;
    private User currentUser;

    private SessionManager() {
      //  System.out.println("SessionManager instance created.");
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        // System.out.println("SessionManager getInstance() called.");
        return instance;
    }

    public synchronized void loginUser(User user) {
        this.currentUser = user;
        System.out.println("Synchronized: User " + user.getUsername() + " logged in.");
    }

    public synchronized User getCurrentUser() {
        System.out.println("getCurrentUser() called. Current user: " + (currentUser != null ? currentUser.getUsername() : "null"));
        return currentUser;
    }
}