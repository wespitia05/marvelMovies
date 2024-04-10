package com.example.marvelmovies;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    private TableColumn <Movies, Integer> yearColumn;
    @FXML
    private TableColumn <Movies, Double> salesColumn;
    @FXML
    private TextField titleTF;
    @FXML
    private TextField yearTF;
    @FXML
    private TextField salesTF;
    @FXML
    private MouseEvent selectMovie;

    public void initialize() {
        System.out.println ("initialize called");
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<Movies, String>("title"));
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<Movies, Integer>("year"));
        salesColumn.setCellValueFactory(
                new PropertyValueFactory<Movies,Double>("sales"));
    }

    public void handleListRecordsButton () {
        System.out.println ("handleListRecordsButton called");
    }

    public void handleAddRecordsButton () {
        System.out.println ("handleAddRecordsButton called");

        String title = titleTF.getText();
        int year = Integer.parseInt(yearTF.getText());
        double sales = Double.parseDouble(salesTF.getText());
        Movies movies = new Movies (title, year, sales);
        ObservableList<Movies> m = moviesTV.getItems();
        m.add(movies);

        System.out.println ("movie added:");
        System.out.println ("\ttitle: " + title + "\tyear: " + year + "\tsales: " + sales);

        titleTF.clear();
        yearTF.clear();
        salesTF.clear();
    }

    public void handleDeleteRecordsButton (ActionEvent event) {
        System.out.println ("handleDeleteRecordsButton called");
        Movies selectedItem = moviesTV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObservableList<Movies> m = moviesTV.getItems();
            m.remove(selectedItem);
            System.out.println ("selection that will be deleted:");
            System.out.println ("\ttitle: " + selectedItem.title + "\tyear: " + selectedItem.year + "\tsales: " + selectedItem.sales);
        }
    }

    public void handleCreateTableMenuItem () {
        System.out.println ("handleCreateTableMenuItem called");
    }

    public void handleImportJsonMenuItem () {
        System.out.println ("handleImportJsonMenuItem called");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                readFile(selectedFile);
                System.out.println ("json file successfully displayed in table view");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readFile (File file) {
        try (FileReader reader = new FileReader(file)) {
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(reader).getAsJsonArray();
            ObservableList<Movies> movies = moviesTV.getItems();
            moviesTV.getItems().clear();
            for (JsonElement e : jArray) {
                JsonObject jObj = e.getAsJsonObject();
                String title = jObj.get("title").getAsString();
                int year = jObj.get("year").getAsInt();
                double sales = jObj.get("sales").getAsDouble();
                Movies m = new Movies (title, year, sales);
                movies.add(m);
            }
            System.out.println ("successfully read json file");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleExportJsonMenuItem () {
        System.out.println ("handleExportJsonMenuItem called");
    }

    public void handleExitMenuItem () {
        System.out.println ("handleExitMenuItem called");
        Platform.exit();
    }

    public void handleAboutMenuItem () {
        System.out.println ("handleAboutMenuItem called");
    }
}