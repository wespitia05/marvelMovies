module com.example.marvelmovies {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.marvelmovies to javafx.fxml;
    exports com.example.marvelmovies;
}