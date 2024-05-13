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

    public void setProperties(Stage primaryStage, Stage currentStage) {
        this.primaryStage = primaryStage;
        this.currentStage = currentStage;
    }

    // OBJECTS
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
        if (vars > 2) variables.setText(String.valueOf(vars - 1));
        welcomeText.setText(variables.getText());
    }

    @FXML
    protected void onVarKeyTyped() {
        String text = variables.getText();
        if (!text.matches("\\d*")) {
            variables.setText(text.replaceAll("\\D", ""));
        }
        welcomeText.setText(variables.getText());
    }

    @FXML
    protected void onVarKeyReleased() {
        int vars = Integer.parseInt(variables.getText());
        if (vars < 2) variables.setText("2");
        else if (vars > 16) variables.setText("16");
        welcomeText.setText(variables.getText());
    }

    @FXML
    protected void onVarUpButtonClicked() {
        int vars = Integer.parseInt(variables.getText());
        if (vars < 16) variables.setText(String.valueOf(vars + 1));
        welcomeText.setText(variables.getText());
    }

    @FXML
    protected void onLimitDownButtonClicked() {
        int limits = Integer.parseInt(limitations.getText());
        limitations.setText(String.valueOf(limits - 1));
        welcomeText.setText(limitations.getText());
    }

    @FXML
    protected void onLimitKeyTyped() {
        String text = limitations.getText();
        if (!text.matches("\\d*")) {
            limitations.setText(text.replaceAll("\\D", ""));
        }
        welcomeText.setText(limitations.getText());
    }

    @FXML
    protected void onLimitKeyReleased() {
        int limits = Integer.parseInt(limitations.getText());
        if (limits < 1) limitations.setText("1");
        else if (limits > 15) limitations.setText("15");
        welcomeText.setText(limitations.getText());
    }

    @FXML
    protected void onLimitUpButtonClicked() {
        int limits = Integer.parseInt(limitations.getText());
        limitations.setText(String.valueOf(limits + 1));
        welcomeText.setText(limitations.getText());
    }

    // CHOICE BOXES
    @FXML
    protected void onTypeChoiceChanged() {
        welcomeText.setText(taskType.getValue());
    }

    @FXML
    protected void onWayChoiceChanged() {
        welcomeText.setText(solutionWay.getValue());
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
