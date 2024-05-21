package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.windows.EnterTaskWindow;

public class EnterTaskController {

    private Stage currentStage;
    private Stage primaryStage;

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

    public void setProperties(Stage primaryStage, Stage currentStage) {
        this.primaryStage = primaryStage;
        this.currentStage = currentStage;
    }

    // MENU
    @FXML
    protected void onDownloadMenuClicked() {

    }

    @FXML
    protected void onCloseMenuClicked() {
        currentStage.close();
    }

    @FXML
    protected void onAboutMenuClicked() {

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
        EnterTaskWindow window = new EnterTaskWindow(primaryStage, currentStage);
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
        return task;
    }
}
