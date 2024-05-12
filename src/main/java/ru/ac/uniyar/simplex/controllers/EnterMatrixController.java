package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.TaskEntity;

public class EnterMatrixController {

    private Stage primaryStage;

    private Stage currentStage;

    private TaskEntity task;

    public void setProperties(Stage primaryStage, Stage currentStage, TaskEntity task) {
        this.primaryStage = primaryStage;
        this.currentStage = currentStage;
        this.task = task;
        initialize();
    }

    @FXML
    private GridPane matrixFun;

    @FXML
    private GridPane matrix;


    @FXML
    private Label welcomeText;

    private void initialize() {

        for (int i = 0; i <= task.getVariables(); i++) {
            String columnHeader;
            if (i == 0) columnHeader = " ";
            else columnHeader = "   c" + i;
            Label columnLabel = new Label(columnHeader);
            matrixFun.add(columnLabel, i, 0);
        }

        for (int j = 0; j <= task.getVariables() + 1; j++) {
            if (j == 0) {
                Label rowLabel = new Label("f(x)");
                matrixFun.add(rowLabel, j, 1); // Добавляем заголовок строки
            } else if (j == task.getVariables() + 1) {
                Label rowLabel = new Label("--> " + task.getTaskType());
                matrixFun.add(rowLabel, j, 1); // Добавляем заголовок строки
            } else {
                TextField textField = new TextField("0");
                matrixFun.add(textField, j, 1); // Добавляем ячейку с нулевым значением
            }

        }

        String rowHeader1 = "f(x)";
        Label rowLabel1 = new Label(rowHeader1);
        matrixFun.add(rowLabel1, 0, 1); // Добавляем заголовок строки

        // Создание заголовков столбцов
        for (int i = 0; i <= task.getVariables() + 1; i++) {
            String columnHeader;
            if (i == 0) columnHeader = " ";
            else if (i == task.getVariables() + 1) columnHeader = "   b";
            else columnHeader = "   a" + i;
            Label columnLabel = new Label(columnHeader);
            matrix.add(columnLabel, i, 0);
        }

        // Создание заголовков строк и ячеек с нулевыми значениями
        for (int i = 1; i <= task.getLimitations(); i++) {
            String rowHeader = "f" + i + "(x)";
            Label rowLabel = new Label(rowHeader);
            matrix.add(rowLabel, 0, i); // Добавляем заголовок строки

            for (int j = 1; j <= task.getVariables() + 1; j++) {
                TextField textField = new TextField("0");
                matrix.add(textField, j, i); // Добавляем ячейку с нулевым значением
            }
        }
    }
}
