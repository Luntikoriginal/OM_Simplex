package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.windows.EnterTaskWindow;

public class HelloController {

    private Stage primaryStage;

    public void setProperties(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onEnterButtonClick() {
        welcomeText.setText("Ввести вручную");
        EnterTaskWindow window = new EnterTaskWindow(primaryStage);
        window.displayEnterSettings();
    }

    @FXML
    protected void onDownloadButtonClick() {
        welcomeText.setText("Загрузить из файла");
    }

    @FXML
    protected void onAboutButtonClick() {
        welcomeText.setText("Справка");
    }
}