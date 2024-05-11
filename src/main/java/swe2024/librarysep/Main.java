package swe2024.librarysep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import swe2024.librarysep.Server.RMIClient;
import swe2024.librarysep.View.DashboardController;


import swe2024.librarysep.Model.BookService;
import swe2024.librarysep.ViewModel.DashboardViewModel;
import swe2024.librarysep.Model.FactoryService;

public class Main extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/swe2024/librarysep/View/Dashboard.fxml"));
                Parent root = loader.load();
                DashboardController controller = loader.getController();

                BookService bookService = FactoryService.getBookService();
                DashboardViewModel viewModel = new DashboardViewModel(bookService);
                controller.setViewModel(viewModel);

                primaryStage.setScene(new Scene(root));
                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}
  /*  @Override
        public void start(Stage primaryStage) throws Exception {
                RMIClient client = new RMIClient();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/swe2024/librarysep/View/Dashboard.fxml"));
                Parent root = loader.load();

                DashboardController controller = loader.getController();
                controller.initClient(client);

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/swe2024/librarysep/Css/styles.css").toString());
                primaryStage.setScene(scene);
                primaryStage.setTitle("Library System");
                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
} */
