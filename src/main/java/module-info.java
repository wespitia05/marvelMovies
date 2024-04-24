module com.example.marvelmovies {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;
    requires com.healthmarketscience.jackcess;


    opens com.example.marvelmovies to javafx.fxml, com.google.gson;
    exports com.example.marvelmovies;
}