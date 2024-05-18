package swe2024.librarysep.Model;

public class User {
    private int userId;
    private String username;
    private String password;

    // This user is for user registration
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // this user constructor is for identifying the user for actions
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


