package swe2024.librarysep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import swe2024.librarysep.View.DashboardController;
import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.ViewModel.DashboardViewModel;
import swe2024.librarysep.Server.RMIBookServiceFactory;

public class Main extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/swe2024/librarysep/View/Dashboard.fxml"));
                Parent root = loader.load();
                DashboardController controller = loader.getController();

                BookService bookService = RMIBookServiceFactory.getBookService();
                DashboardViewModel viewModel = new DashboardViewModel(bookService);
                controller.setViewModel(viewModel);

                primaryStage.setScene(new Scene(root));
                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}
