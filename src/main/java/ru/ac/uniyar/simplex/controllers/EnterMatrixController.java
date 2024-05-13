package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.windows.SimplexWindow;

import java.util.ArrayList;
import java.util.Arrays;

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
    private Button headButton;

    @FXML
    private Button bottomButton;

    @FXML
    private GridPane matrixFun;

    @FXML
    private GridPane matrix;


    @FXML
    private Label welcomeText;

    private void initialize() {

        if (task.getLimitations() > 12) {
            bottomButton.setVisible(false);
            headButton.setVisible(true);
        }

        // f(x) table
        for (int i = 0; i <= task.getVariables(); i++) {
            String columnHeader;
            if (i == 0) columnHeader = " ";
            else columnHeader = "x" + i;
            Label columnLabel = new Label(columnHeader);
            matrixFun.add(columnLabel, i, 0);
            GridPane.setHalignment(columnLabel, HPos.CENTER);
        }

        for (int i = 0; i <= task.getVariables() + 1; i++) {
            if (i == 0) {
                Label rowLabel = new Label("f(x)");
                matrixFun.add(rowLabel, i, 1);
                GridPane.setHalignment(rowLabel, HPos.CENTER);
            } else if (i == task.getVariables() + 1) {
                Label rowLabel = new Label("--> " + task.getTaskType());
                matrixFun.add(rowLabel, i, 1);
            } else {
                TextField textField = new TextField("0");
                textField.setPrefWidth(50);
                matrixFun.add(textField, i, 1);
            }
        }

        if (!task.getAutoBases()) {
            for (int i = 0; i <= task.getVariables(); i++) {
                if (i != 0) {
                    CheckBox checkBox = new CheckBox();
                    matrixFun.add(checkBox, i, 2);
                    GridPane.setHalignment(checkBox, HPos.CENTER);
                } else {
                    Label rowLabel = new Label("Базисы:");
                    matrixFun.add(rowLabel, i, 2);
                    GridPane.setHalignment(rowLabel, HPos.CENTER);
                }
            }
        }

        // Fi(x) table
        for (int i = 0; i <= task.getVariables() + 1; i++) {
            String columnHeader;
            if (i == 0) columnHeader = " ";
            else if (i == task.getVariables() + 1) columnHeader = "b";
            else columnHeader = "a" + i;
            Label columnLabel = new Label(columnHeader);
            matrix.add(columnLabel, i, 0);
            GridPane.setHalignment(columnLabel, HPos.CENTER);
        }


        for (int i = 1; i <= task.getLimitations(); i++) {
            String rowHeader = "f" + i + "(x)";
            Label rowLabel = new Label(rowHeader);
            matrix.add(rowLabel, 0, i);
            GridPane.setHalignment(rowLabel, HPos.CENTER);

            for (int j = 1; j <= task.getVariables() + 1; j++) {
                TextField textField = new TextField("0");
                textField.setPrefWidth(50);
                matrix.add(textField, j, i);
            }
        }
    }

    @FXML
    protected void onSaveButtonClicked() {
        Fraction[] function = readFunction();
        Fraction[][] limitsMatrix = readLimitsMatrix();
        task.setFunction(function);
        if (!task.getAutoBases()) {
            ArrayList<Integer> bases = readBases();
            task.setBases(bases);
        }
        task.setMatrix(limitsMatrix);
        SimplexWindow window = new SimplexWindow(primaryStage);
        window.display(task);
        currentStage.close();
    }

    private Fraction[] readFunction() {
        Fraction[] function = new Fraction[task.getVariables()];
        int column = 0;
        for (Node node : matrixFun.getChildren()) {
            if (node instanceof TextField textField) {
                String value = textField.getText();
                String[] fractionParts = value.split("/");
                if (fractionParts.length == 1) {
                    function[column] = new Fraction(Integer.parseInt(fractionParts[0]), 1);
                } else {
                    function[column] = new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
                    function[column].reduction();
                }
                column++;
            }
        }
        System.out.println(Arrays.toString(function));
        return function;
    }

    private Fraction[][] readLimitsMatrix() {
        Fraction[][] limitsMatrix = new Fraction[task.getLimitations()][];
        ArrayList<String> rowValues = new ArrayList<>();
        int row = 0;
        for (Node node : matrix.getChildren()) {
            if (node instanceof TextField textField) {
                String value = textField.getText();
                rowValues.add(value);
                if (rowValues.size() == task.getVariables() + 1) {
                    limitsMatrix[row] = new Fraction[rowValues.size()];
                    for (int i = 0; i < rowValues.size(); i++) {
                        String[] fractionParts = rowValues.get(i).split("/");
                        if (fractionParts.length == 1) {
                            limitsMatrix[row][i] = new Fraction(Integer.parseInt(fractionParts[0]), 1);
                        } else {
                            limitsMatrix[row][i] = new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
                            limitsMatrix[row][i].reduction();
                        }
                    }
                    rowValues.clear();
                    row++;
                }
            }
        }
        System.out.println(Arrays.deepToString(limitsMatrix));
        return limitsMatrix;
    }

    private ArrayList<Integer> readBases() {
        ArrayList<Integer> bases = new ArrayList<>();
            for (Node node : matrixFun.getChildren()) {
                if (node instanceof CheckBox checkBox && checkBox.isSelected())
                    bases.add(GridPane.getColumnIndex(checkBox));
            }
        System.out.println(bases);
        return bases;
    }
}
