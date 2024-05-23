package swe2024.librarysep.Utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import swe2024.librarysep.Database.DatabaseConnection;
import swe2024.librarysep.Database.UserService;
import swe2024.librarysep.Model.Book;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Model.User;
import swe2024.librarysep.Server.RMIBookServiceFactory;
import swe2024.librarysep.View.*;
import swe2024.librarysep.ViewModel.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * SceneManager class is responsible for injecting dependencies into various scenes.
 * <p>
 * Dependency Injection: By managing the injection of dependencies (like view models) into controllers,
 * the SceneManager helps maintain a clean separation of concerns and ensures that controllers are
 * decoupled from the specifics of dependency creation.
 */

@SuppressWarnings("CallToPrintStackTrace")

public class SceneManager {

    private static Stage primaryStage;

    /**
     * Sets the primary stage for the application.
     *
     * @param stage the primary stage to set
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Gets the primary stage of the application.
     *
     * @return the primary stage
     */
    private static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Shows the login scene.
     */
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

    /**
     * Shows the admin dashboard scene.
     */
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

    /**
     * Shows the user dashboard scene.
     */
    public static void showUserDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/userDashboard.fxml"));
            Parent root = loader.load();
            UserDashboardController controller = loader.getController();
            BookService bookService = RMIBookServiceFactory.getBookService();
            UserDashboardViewModel viewModelUser = new UserDashboardViewModel(bookService);
            controller.setViewModel(viewModelUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(SceneManager.class.getResource("/swe2024/librarysep/Css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the registration scene.
     */
    public static void showRegistration() {
        try {
            Connection connection = DatabaseConnection.connect();
            UserService userService = new UserService(connection);
            RegistrationViewModel registrationViewModel = new RegistrationViewModel(userService);

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/userRegistration.fxml"));
            loader.setControllerFactory(type -> {
                if (type == RegistrationController.class) {
                    RegistrationController controller = new RegistrationController();
                    controller.setViewModel(registrationViewModel);
                    return controller;
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

    /**
     * Shows the edit book scene with the specified book.
     *
     * @param book the book to edit
     */
    public static void showEditBook(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/editBook.fxml"));
            Parent root = loader.load();

            EditBookController controller = loader.getController();
            EditBookViewModel viewModel = new EditBookViewModel(RMIBookServiceFactory.getBookService());
            controller.setViewModel(viewModel);
            controller.setBook(book);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the add book scene.
     */
    public static void showAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/addBook.fxml"));
            Parent root = loader.load();

            AddBookController controller = loader.getController();
            AddBookViewModel viewModel = new AddBookViewModel(RMIBookServiceFactory.getBookService());
            controller.setViewModel(viewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the my profile scene for the specified user.
     *
     * @param currentUser the current user
     */
    public static void showMyProfile(User currentUser) {
        try {
            if (currentUser == null) {
                showLogin();
                return;
            }

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/swe2024/librarysep/View/myProfile.fxml"));
            Parent root = loader.load();
            MyProfileController controller = loader.getController();
            MyProfileViewModel viewModel = new MyProfileViewModel(RMIBookServiceFactory.getBookService());
            controller.setViewModel(viewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
