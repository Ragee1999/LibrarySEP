module swe2024.librarysep {
    requires javafx.controls;
    requires javafx.fxml;


    opens swe2024.librarysep to javafx.fxml;
    opens swe2024.librarysep.View to javafx.fxml;
    exports swe2024.librarysep;
    exports swe2024.librarysep.View;


}