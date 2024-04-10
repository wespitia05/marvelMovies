package com.example.marvelmovies;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private TableColumn <String, Movies> titleColumn;
    @FXML
    private TableColumn <Integer, Movies> yearColumn;
    @FXML
    private TableColumn <Double, Movies> salesColumn;

    public void initialize() {
        System.out.println ("Initialize called");
    }

    public void handleListRecordsButton () {
        System.out.println ("handleListRecordsButton called");
    }

    public void handleAddRecordsButton () {
        System.out.println ("handleAddRecordsButton called");
    }

    public void handleDeleteRecordsButton () {
        System.out.println ("handleDeleteRecordsButton called");
    }

    public void handleCreateTableMenuItem () {
        System.out.println ("handleCreateTableMenuItem called");
    }

    public void handleImportJsonMenuItem () {
        System.out.println ("handleImportJsonMenuItem called");
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