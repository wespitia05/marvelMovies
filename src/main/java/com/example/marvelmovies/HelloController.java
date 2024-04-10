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

import java.io.*;

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
    }

    public void handleAddRecordsButton () {
        System.out.println ("handleAddRecordsButton called");

        String newTitle = titleTF.getText();
        String newYear = yearTF.getText();
        String newSales = salesTF.getText();
        if (validInput(newTitle, newYear, newSales)) {
            Movies movies = new Movies (newTitle, newYear, newSales);
            ObservableList<Movies> m = moviesTV.getItems();
            m.add(movies);

            System.out.println ("movie added:");
            System.out.println ("\ttitle: " + newTitle + "\tyear: " + newYear + "\tsales: " + newSales);
            statusLabel.setText("A movie has been inserted: \"" + newTitle + "\"");

            titleTF.clear();
            yearTF.clear();
            salesTF.clear();
        }
        else {
            System.out.println ("invalid input");
        }
    }

    private boolean validInput (String title, String year, String sales) {
        if (title.isEmpty() || !title.matches("[A-Z][\\W\\w\\s]*")) {
            return false;
        }
        if (year.isEmpty() || !year.matches("\\d*")) {
            return false;
        }
        if (sales.isEmpty() || !sales.matches("\\d+(\\.\\d+)?")) {
            return false;
        }
        return true;
    }

    public void handleDeleteRecordsButton (ActionEvent event) {
        System.out.println ("handleDeleteRecordsButton called");
        Movies selectedItem = moviesTV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObservableList<Movies> m = moviesTV.getItems();
            m.remove(selectedItem);
            System.out.println ("selection that will be deleted:");
            System.out.println ("\ttitle: " + selectedItem.title + "\tyear: " + selectedItem.year + "\tsales: " + selectedItem.sales);
            statusLabel.setText("A movie has been deleted: \"" + selectedItem.title + "\"");
        }
    }

    public void handleCreateTableMenuItem () {
        System.out.println ("handleCreateTableMenuItem called");
    }

    public void handleImportJsonMenuItem () {
        System.out.println ("handleImportJsonMenuItem called");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                readFile(selectedFile);
                System.out.println ("json file successfully displayed in table view");
                statusLabel.setText("Imported data from " + selectedFile.getAbsolutePath());
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
                String year = jObj.get("year").getAsString();
                String sales = jObj.get("sales").getAsString();
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            saveFile(selectedFile);
            System.out.println ("exported json file successfully");
            statusLabel.setText("Exported data to " + selectedFile.getAbsolutePath());
        }
    }

    private void saveFile (File file) {
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder toJson = new StringBuilder();
            toJson.append("[\n");
            ObservableList<Movies> m = moviesTV.getItems();
            for (Movies movies : m) {
                toJson.append ("  {\n")
                        .append ("    \"sales\": ").append (movies.getSales()).append (", \n")
                        .append ("    \"year\": ").append (movies.getYear()).append (", \n")
                        .append ("    \"title\": \"").append (movies.getTitle()).append ("\" \n")
                        .append ("  },\n");
            }
            if (!m.isEmpty()) {
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
    }
}