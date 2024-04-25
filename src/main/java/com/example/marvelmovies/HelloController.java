package com.example.marvelmovies;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import com.google.gson.*;

import java.io.*;
import java.sql.*;
import java.util.List;

public class HelloController {
    @FXML
    private Button listRecordsButton;
    @FXML
    private Button addRecordsButton;
    @FXML
    private Button deleteRecordsButton;
    @FXML
    private TableView <Movies> moviesTV;
    @FXML
    private MenuItem createTableMI;
    @FXML
    private MenuItem importJsonMI;
    @FXML
    private MenuItem exportJsonMI;
    @FXML
    private MenuItem exitMI;
    @FXML
    private MenuItem aboutMI;
    @FXML
    private TableColumn <Movies, String> titleColumn;
    @FXML
    private TableColumn <Movies, String> yearColumn;
    @FXML
    private TableColumn <Movies, String> salesColumn;
    @FXML
    private TextField titleTF;
    @FXML
    private TextField yearTF;
    @FXML
    private TextField salesTF;
    @FXML
    private Label statusLabel;

    public void initialize() {
        System.out.println ("initialize called");
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<Movies, String>("title"));
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<Movies, String>("year"));
        salesColumn.setCellValueFactory(
                new PropertyValueFactory<Movies, String>("sales"));
    }

    public void handleListRecordsButton () {
        System.out.println ("handleListRecordsButton called");

        ObservableList<Movies> movies = getMoviesFromDB();
        moviesTV.setItems(movies);

        statusLabel.setText("movie table displayed");
    }

    public ObservableList<Movies> getMoviesFromDB() {
        ObservableList<Movies> movies = FXCollections.observableArrayList();
        String dbFilePath = ".//MoviesDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        String sql = "SELECT Title, Year, Sales FROM MoviesDB";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery(sql)) {

            while (result.next()) {
                String title = result.getString("Title");
                int year = result.getInt("Year");
                double sales = result.getDouble("Sales");
                movies.add(new Movies(title, year, sales));
            }
            System.out.println ("data displayed successfully into table view from db");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return movies;
    }

    public void handleAddRecordsButton() {
        System.out.println ("handleAddRecordsButton called");
        String dbFilePath = ".//MoviesDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;

        String newTitle = titleTF.getText();
        String newYear = yearTF.getText();
        String newSales = salesTF.getText();

        List<String> errors = Validation.validInput(newTitle, newYear, newSales);
        if (errors.isEmpty()) {
            try {
                Connection conn = DriverManager.getConnection(databaseURL);
                insertData(conn, newTitle, Integer.parseInt(newYear), Double.parseDouble(newSales));
                System.out.println ("inserted movie into database successfully");
                statusLabel.setText("a movie has been inserted: \"" + newTitle + "\"");
                moviesTV.setItems(getMoviesFromDB());
                titleTF.clear();
                yearTF.clear();
                salesTF.clear();
            } catch (Exception e) {
                errors.add("error inserting movie into database: " + e.getMessage());
                errorAlert(errors);
            }
        } else {
            errorAlert(errors);
        }
    }

    public void errorAlert (List<String> error) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid Input");
        String content = String.join("\n", error);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleDeleteRecordsButton (ActionEvent event) {
        System.out.println ("handleDeleteRecordsButton called");
        Movies selectedItem = moviesTV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            deleteMovieFromDB(selectedItem.getTitle());
            System.out.println ("successfully removed movie from database");
            ObservableList<Movies> m = moviesTV.getItems();
            m.remove(selectedItem);
            System.out.println ("selection that will be deleted:");
            System.out.println ("\ttitle: " + selectedItem.title + "\tyear: " + selectedItem.year + "\tsales: " + selectedItem.sales);
            statusLabel.setText("A movie has been deleted: \"" + selectedItem.title + "\"");
        }
    }

    private void deleteMovieFromDB(String title) {
        String dbFilePath = ".//MoviesDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        String sql = "DELETE FROM MoviesDB WHERE Title = ?";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
             preparedStatement.setString(1, title);
        } catch (SQLException e) {
            System.err.println("error deleting movie from database: " + e.getMessage());
        }
    }

    public void handleCreateTableMenuItem () {
        System.out.println ("handleCreateTableMenuItem called");

        // creating database and table

        String dbFilePath = ".//MoviesDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            try (Database db = DatabaseBuilder.create(Database.FileFormat.V2010, new File(dbFilePath))) {
                System.out.println("The database file has been created.");
                Connection conn = DriverManager.getConnection(databaseURL);
                String sql;
                sql = "CREATE TABLE MoviesDB (Title nvarchar(255), Year INT, Sales DOUBLE)";
                Statement createTableStatement = conn.createStatement();
                createTableStatement.execute(sql);
                conn.commit();
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println ("database table already exists");
        }
    }

    public static void insertData(Connection conn, String title, int year, double sales) {
        // the three ? characters will be filled with data by the preparedStatement class
        String sql = "INSERT INTO MoviesDB (Title, Year, Sales) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            // declares that conn has a valid connection to the database
            preparedStatement = conn.prepareStatement(sql);
            // replaces the first ? in the SQL string with data from the first variable
            preparedStatement.setString(1, title);
            // replaces the second ? in the SQL string with data from the second variable
            preparedStatement.setInt(2, year);
            // replaces the third ? in the SQL string with data from the third variable
            preparedStatement.setDouble(3, sales);
            // once all data is inserted, it will execute the preparedStatement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleImportJsonMenuItem() {
        System.out.println("handleImportJsonMenuItem called");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String dbFilePath = ".//MoviesDB.accdb";
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;

            clearDataBase();

            try (Connection conn = DriverManager.getConnection(databaseURL)) {
                Gson gson = new Gson();
                try (Reader reader = new FileReader(selectedFile)) {
                    Movies[] movies = gson.fromJson(reader, Movies[].class);
                    for (Movies movie : movies) {
                        insertData(conn, movie.getTitle(), movie.getYear(), movie.getSales());
                    }
                    moviesTV.setItems(getMoviesFromDB());
                    conn.commit();
                    System.out.println("data loaded from json to database successfully");
                    statusLabel.setText("imported data from " + selectedFile.getAbsolutePath());
                } catch (IOException ex) {
                    System.err.println("error reading json file: " + ex.getMessage());
                }
            } catch (SQLException ex) {
                throw new RuntimeException("database connection failed", ex);
            }
        }
    }

    public void clearDataBase() {
        String dbFilePath = ".//MoviesDB.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            String sql = "DELETE FROM MoviesDB";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                int rowsDeleted = preparedStatement.executeUpdate();
                System.out.println("number of rows deleted: " + rowsDeleted);
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to clear database", e);
        }
    }

    public void handleExportJsonMenuItem () {
        System.out.println ("handleExportJsonMenuItem called");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            ObservableList<Movies> movies = getMoviesFromDB();
            saveFile(selectedFile, movies);
            System.out.println ("exported json file successfully");
            statusLabel.setText("exported data to " + selectedFile.getAbsolutePath());
        }
    }

    private void saveFile (File file, ObservableList<Movies> movies) {
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder toJson = new StringBuilder();
            movies = moviesTV.getItems();
            toJson.append ("[\n");
            for (Movies m : movies) {
                toJson.append ("  {\n")
                        .append ("    \"sales\": ").append (m.getSales()).append (", \n")
                        .append ("    \"year\": ").append (m.getYear()).append (", \n")
                        .append ("    \"title\": \"").append (m.getTitle()).append ("\" \n")
                        .append ("  },\n");
            }
            if (!movies.isEmpty()) {
                toJson.setLength(toJson.length() - 2);
            }
            toJson.append ("\n]");
            writer.write (toJson.toString());
            System.out.println ("movie data saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleExitMenuItem () {
        System.out.println ("handleExitMenuItem called");
        Platform.exit();
    }

    public void handleAboutMenuItem () {
        System.out.println ("handleAboutMenuItem called");
        integrityAlert();
    }

    public void integrityAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Movie Database");
        alert.setHeaderText("Name and Integrity Statement");
        alert.setContentText("William Espitia\n\nI certify that this submission is my original work.");
        alert.showAndWait();
    }
}