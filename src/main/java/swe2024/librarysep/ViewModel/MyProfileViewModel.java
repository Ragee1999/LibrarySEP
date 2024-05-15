package swe2024.librarysep.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import swe2024.librarysep.Model.User;

public class MyProfileViewModel {
    private StringProperty username = new SimpleStringProperty();
    private User currentUser;


    public MyProfileViewModel(User currentUser) {
        this.currentUser = currentUser;
        this.username.set(currentUser.getUsername());
    }

    public StringProperty usernameProperty() {
        return username;
    }
}

