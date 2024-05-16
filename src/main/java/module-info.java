module swe2024.librarysep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.rmi;


    exports swe2024.librarysep to javafx.graphics;
    exports swe2024.librarysep.View to javafx.fxml;
    opens swe2024.librarysep.Model to javafx.base, javafx.fxml;
    opens swe2024.librarysep.View to javafx.fxml;
    opens swe2024.librarysep.ViewModel to javafx.fxml;


    exports swe2024.librarysep.Server to java.rmi;
    opens swe2024.librarysep.Server to javafx.base, javafx.fxml;
    opens swe2024.librarysep.Database to javafx.base, javafx.fxml;
    opens swe2024.librarysep.Utility to javafx.base, javafx.fxml;
}