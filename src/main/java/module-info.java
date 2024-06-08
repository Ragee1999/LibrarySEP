module swe2024.librarysep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.rmi;
    requires org.mockito;
    requires org.junit.jupiter.api;

    exports swe2024.librarysep to javafx.graphics;
    exports swe2024.librarysep.View to javafx.fxml;
    exports swe2024.librarysep.Model to java.rmi;
    exports swe2024.librarysep.Server to java.rmi;

    // Export the Test package to the JUnit platform
    exports swe2024.librarysep.Test to org.junit.platform.commons;

    opens swe2024.librarysep.Model to javafx.base, javafx.fxml, org.mockito;
    opens swe2024.librarysep.View to javafx.fxml, org.mockito;
    opens swe2024.librarysep.ViewModel to javafx.fxml, org.mockito, org.junit.platform.commons;
    opens swe2024.librarysep.Server to javafx.base, javafx.fxml, org.mockito;
    opens swe2024.librarysep.Database to javafx.base, javafx.fxml, org.mockito;
    opens swe2024.librarysep.Utility to javafx.base, javafx.fxml, org.mockito;
}