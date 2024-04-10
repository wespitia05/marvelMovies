module com.example.marvelmovies {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.marvelmovies to javafx.fxml;
    exports com.example.marvelmovies;
}