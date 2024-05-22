package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.utils.FileUtils;
import ru.ac.uniyar.simplex.windows.*;

import java.io.IOException;
import java.util.ArrayList;

public class EnterTaskController {

    private Stage currentStage;

    @FXML
    private Label welcomeText;
    @FXML
    private TextField variables;
    @FXML
    private TextField limitations;
    @FXML
    private ChoiceBox<String> taskType;
    @FXML
    private ChoiceBox<String> solutionWay;
    @FXML
    private CheckBox autoBases;

    public void setProperties(Stage currentStage) {
        this.currentStage = currentStage;
    }

    // MENU
    @FXML
    protected void onDownloadMenuClicked() {
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
    protected void onCloseMenuClicked() {
        HelloWindow window = new HelloWindow();
        window.display();
        currentStage.close();
    }

    @FXML
    protected void onAboutMenuClicked() {
        SettingsReferenceWindow window = new SettingsReferenceWindow();
        window.display();
    }

    @FXML
    protected void onReferenceMenuClicked() {
        AboutWindow window = new AboutWindow();
        window.display();
    }

    // TEXT FIELDS
    @FXML
    protected void onVarDownButtonClicked() {
        int vars = Integer.parseInt(variables.getText());
        if (vars > Integer.parseInt(limitations.getText()) + 1)
            variables.setText(String.valueOf(vars - 1));
    }

    @FXML
    protected void onVarUpButtonClicked() {
        int vars = Integer.parseInt(variables.getText());
        if (vars < 16) variables.setText(String.valueOf(vars + 1));
    }

    @FXML
    protected void onLimitDownButtonClicked() {
        int limits = Integer.parseInt(limitations.getText());
        if (limits > 1) limitations.setText(String.valueOf(limits - 1));
    }

    @FXML
    protected void onLimitUpButtonClicked() {
        int limits = Integer.parseInt(limitations.getText());
        if (limits < Integer.parseInt(variables.getText()) - 1)
            limitations.setText(String.valueOf(limits + 1));
    }

    // SAVE BUTTON
    @FXML
    protected void onSaveButtonClicked() {
        TaskEntity task = saveTaskSettings();
        EnterTaskWindow window = new EnterTaskWindow(currentStage);
        window.displayEnterMatrix(task);
    }

    private TaskEntity saveTaskSettings() {
        TaskEntity task = new TaskEntity();
        task.setVariables(Integer.parseInt(variables.getText()));
        task.setLimitations(Integer.parseInt(limitations.getText()));
        if (taskType.getValue().equals("Минимизировать")) task.setTaskType("min");
        else task.setTaskType("max");
        if (solutionWay.getValue().equals("Пошаговый")) task.setSolutionWay("steps");
        else task.setSolutionWay("auto");
        task.setAutoBases(autoBases.isSelected());
        if (autoBases.isSelected()) {
            task.setBases(new ArrayList<>());
            for (int i = 1; i <= task.getLimitations(); i++) {
                task.getBases().add(i);
            }
        }
        return task;
    }
}
