package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.utils.FileUtils;
import ru.ac.uniyar.simplex.windows.EnterTaskWindow;
import ru.ac.uniyar.simplex.windows.SimplexWindow;

import java.io.IOException;
import java.util.Arrays;

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
    protected void onDownloadButtonClick() throws IOException {
        TaskEntity task = FileUtils.readTaskFromJSON();
        SimplexWindow window = new SimplexWindow();
        window.display(task);
        primaryStage.close();
    }

    @FXML
    protected void onAboutButtonClick() {
        welcomeText.setText("Справка");
    }
}