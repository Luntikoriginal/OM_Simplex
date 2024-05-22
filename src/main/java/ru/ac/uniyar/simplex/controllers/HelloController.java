package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.utils.FileUtils;
import ru.ac.uniyar.simplex.windows.AboutWindow;
import ru.ac.uniyar.simplex.windows.EnterTaskWindow;
import ru.ac.uniyar.simplex.windows.SimplexWindow;

import java.io.IOException;

public class HelloController {

    private Stage currentStage;

    @FXML
    private Label welcomeText;

    public void setProperties(Stage currentStage) {
        this.currentStage = currentStage;
    }

    @FXML
    protected void onEnterButtonClick() {
        welcomeText.setText("Ввести вручную");
        EnterTaskWindow window = new EnterTaskWindow();
        window.displayEnterSettings();
        currentStage.close();
    }

    @FXML
    protected void onDownloadButtonClick() {
        try {
            TaskEntity task = FileUtils.readTaskFromJSON();
            SimplexWindow window = new SimplexWindow();
            window.display(task);
            currentStage.close();
        } catch (IOException e) {
            welcomeText.setText("Не удалось прочитать файл!");
            welcomeText.setTextFill(Color.RED);
        }
    }

    @FXML
    protected void onAboutButtonClick() {
        AboutWindow window = new AboutWindow();
        window.display();
    }
}