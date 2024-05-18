package swe2024.librarysep.Utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import swe2024.librarysep.Database.DatabaseConnection;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Server.RMIBookServiceFactory;
import swe2024.librarysep.View.*;
import swe2024.librarysep.ViewModel.AdminDashboardViewModel;
import swe2024.librarysep.ViewModel.LoginViewModel;
import swe2024.librarysep.ViewModel.MyProfileViewModel;
import swe2024.librarysep.ViewModel.userDashboardViewModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SceneManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void showLogin() {
        try {
            Connection connection = DatabaseConnection.connect();
            UserService userService = new UserService(connection);
            LoginViewModel loginViewModel = new LoginViewModel(userService);

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/userLogin.fxml"));
            loader.setControllerFactory(type -> {
                if (type == LoginController.class) {
                    return new LoginController(loginViewModel);
                } else {
                    try {
                        return type.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/adminDashboard.fxml"));
            Parent root = loader.load();
            AdminDashboardController controller = loader.getController();
            BookService bookService = RMIBookServiceFactory.getBookService();
            AdminDashboardViewModel viewModel = new AdminDashboardViewModel(bookService);
            controller.setViewModel(viewModel);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(SceneManager.class.getResource("/swe2024/librarysep/Css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showUserDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/userDashboard.fxml"));
            Parent root = loader.load();
            userDashboardController controller = loader.getController();
            BookService bookService = RMIBookServiceFactory.getBookService();
            userDashboardViewModel viewModelUser = new userDashboardViewModel(bookService);
            controller.setViewModel(viewModelUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(SceneManager.class.getResource("/swe2024/librarysep/Css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showRegistration() {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/swe2024/librarysep/View/userRegistration.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAddBook() {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/swe2024/librarysep/View/addBook.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EditBookController showEditBook() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/editBook.fxml"));
            Parent root = loader.load();
            EditBookController controller = loader.getController();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showMyProfile(User currentUser) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/myProfile.fxml"));
            Parent root = loader.load();
            MyProfileController controller = loader.getController();
            MyProfileViewModel viewModel = new MyProfileViewModel(currentUser, RMIBookServiceFactory.getBookService());
            controller.setViewModel(viewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}