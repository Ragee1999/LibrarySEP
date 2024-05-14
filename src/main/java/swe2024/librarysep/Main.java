package swe2024.librarysep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import swe2024.librarysep.View.DashboardController;
import swe2024.librarysep.View.userDashboardController;
import swe2024.librarysep.ViewModel.DashboardViewModel;

import swe2024.librarysep.ViewModel.userDashboardViewModel;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.Server.RMIBookServiceFactory;

public class Main extends Application {

        private static Stage primaryStage;

        @Override
        public void start(Stage primaryStage) {
                Main.primaryStage = primaryStage;
                showLogin(); // Shows the login screen when opening the application
        }

        public static void main(String[] args) {
                launch(args);
        }

        public static void showLogin() {
                try {
                        Parent root = FXMLLoader.load(Main.class.getResource("/swe2024/librarysep/View/userLogin.fxml"));
                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static void showAdminDashboard() {
                try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/swe2024/librarysep/View/AdminDashboard.fxml"));
                        Parent root = loader.load();
                        DashboardController controller = loader.getController();

                        BookService bookService = RMIBookServiceFactory.getBookService();
                        DashboardViewModel viewModel = new DashboardViewModel(bookService);
                        controller.setViewModel(viewModel);

                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static void showDashboard() {
                try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/swe2024/librarysep/View/userDashboard.fxml"));
                        Parent root = loader.load();
                        userDashboardController controller = loader.getController();

                        BookService bookService = RMIBookServiceFactory.getBookService();
                        userDashboardViewModel viewModelUser = new userDashboardViewModel(bookService);
                        controller.setViewModel(viewModelUser);

                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static void showRegistration() {
                try {
                        Parent root = FXMLLoader.load(Main.class.getResource("/swe2024/librarysep/View/userRegistration.fxml"));
                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static void ShowAddBook() {
                try {
                        Parent root = FXMLLoader.load(Main.class.getResource("/swe2024/librarysep/View/addBook.fxml"));
                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}