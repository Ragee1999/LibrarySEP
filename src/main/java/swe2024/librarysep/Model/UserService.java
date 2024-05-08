package swe2024.librarysep.Model;

public class UserService {
    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }
}
