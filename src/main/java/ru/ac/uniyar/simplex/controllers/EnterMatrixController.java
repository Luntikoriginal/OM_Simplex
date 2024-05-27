package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.exceptions.BadFieldValueException;
import ru.ac.uniyar.simplex.exceptions.BasesFormatException;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;
import ru.ac.uniyar.simplex.utils.FileUtils;
import ru.ac.uniyar.simplex.windows.*;

import java.io.IOException;
import java.util.ArrayList;

public class EnterMatrixController {

    private Stage currentStage;
    private TaskEntity task;

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

    public void setProperties(Stage currentStage, TaskEntity task) {
        this.currentStage = currentStage;
        this.task = task;
        initialize();
    }

    private void initialize() {
        if (task.getLimitations() > 12) {
            bottomButton.setVisible(false);
            headButton.setVisible(true);
        }

        printFXHeader();

        printFXBody();

        if (!task.getAutoBases() && task.getSolutionType().equals("simplex"))
            printFXCheckBoxes();

        printLimitsHeader();

        printLimitsBody();

        adjustWindowSize();
    }

    private void adjustWindowSize() {
        int fieldWith = 60;
        int fieldHeight = 35;

        double newWidth = fieldWith * (task.getVariables() + 2) + 100;
        double newHeight = fieldHeight * (task.getLimitations() + 4) + 200;

        currentStage.setMinWidth(newWidth);
        currentStage.setMinHeight(newHeight);
        currentStage.setWidth(newWidth);
        currentStage.setHeight(newHeight);
        currentStage.centerOnScreen();
    }


    private void printLimitsBody() {
        for (int i = 1; i <= task.getLimitations(); i++) {
            String rowHeader = "f" + i + "(x)";
            Label rowLabel = new Label(rowHeader);
            rowLabel.setFont(Font.font(14));
            matrix.add(rowLabel, 0, i);
            GridPane.setHalignment(rowLabel, HPos.CENTER);

            for (int j = 1; j <= task.getVariables() + 1; j++) {
                TextField textField = new TextField("0");
                textField.setPrefWidth(50);
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("-?\\d*/?\\d*")) {
                        textField.setText(oldValue);
                    }
                });
                matrix.add(textField, j, i);
            }
        }
    }

    private void printLimitsHeader() {
        for (int i = 0; i <= task.getVariables() + 1; i++) {
            String columnHeader;
            if (i == 0) columnHeader = " ";
            else if (i == task.getVariables() + 1) columnHeader = "b";
            else columnHeader = "a" + i;
            Label columnLabel = new Label(columnHeader);
            columnLabel.setFont(Font.font(14));
            matrix.add(columnLabel, i, 0);
            GridPane.setHalignment(columnLabel, HPos.CENTER);
        }
    }

    private void printFXCheckBoxes() {
        for (int i = 0; i <= task.getVariables(); i++) {
            if (i != 0) {
                CheckBox checkBox = new CheckBox();
                checkBox.setFocusTraversable(false);
                matrixFun.add(checkBox, i, 2);
                GridPane.setHalignment(checkBox, HPos.CENTER);
            } else {
                Label rowLabel = new Label("Базисы:");
                rowLabel.setFont(Font.font(14));
                matrixFun.add(rowLabel, i, 2);
                GridPane.setHalignment(rowLabel, HPos.CENTER);
            }
        }
    }

    private void printFXBody() {
        for (int i = 0; i <= task.getVariables() + 1; i++) {
            if (i == 0) {
                Label rowLabel = new Label("f(x)");
                rowLabel.setFont(Font.font(14));
                matrixFun.add(rowLabel, i, 1);
                GridPane.setHalignment(rowLabel, HPos.CENTER);
            } else if (i == task.getVariables() + 1) {
                Label rowLabel = new Label("--> " + task.getTaskType());
                rowLabel.setFont(Font.font(14));
                matrixFun.add(rowLabel, i, 1);
            } else {
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("-?\\d*/?\\d*")) {
                        textField.setText(oldValue);
                    }
                });
                matrixFun.add(textField, i, 1);
            }
        }
    }

    private void printFXHeader() {
        for (int i = 0; i <= task.getVariables(); i++) {
            String columnHeader;
            if (i == 0) columnHeader = " ";
            else columnHeader = "x" + i;
            Label columnLabel = new Label(columnHeader);
            columnLabel.setFont(Font.font(14));
            matrixFun.add(columnLabel, i, 0);
            GridPane.setHalignment(columnLabel, HPos.CENTER);
        }
    }

    @FXML
    protected void onSaveButtonClicked() {
        try {
            Fraction[] function = readFunction();
            Fraction[][] limitsMatrix = readLimitsMatrix();
            task.setFunction(function);
            if (!task.getAutoBases() && task.getSolutionType().equals("simplex")) {
                ArrayList<Integer> bases = readBases();
                task.setBases(bases);
            }
            task.setMatrix(limitsMatrix);
            SimplexWindow window = new SimplexWindow();
            window.display(task);
            currentStage.close();
        } catch (Exception e) {
            welcomeText.setTextFill(Color.RED);
            welcomeText.setText(e.getMessage());
        }
    }

    private Fraction[] readFunction() throws BadFieldValueException, FractionCreateException {
        Fraction[] function = new Fraction[task.getVariables()];
        int column = 0;
        for (Node node : matrixFun.getChildren()) {
            if (node instanceof TextField textField) {
                String value = textField.getText();
                if (value.isEmpty()) throw new BadFieldValueException("Пустое поле в целевой функции!");
                if (value.equals("0")) throw new BadFieldValueException("Коэфф целевой функции не может быть равен 0");
                String[] fractionParts = value.split("/");
                if (fractionParts.length == 1) {
                    function[column] = new Fraction(Integer.parseInt(fractionParts[0]), 1);
                } else {
                    function[column] = new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
                }
                column++;
            }
        }
        return function;
    }

    private Fraction[][] readLimitsMatrix() throws FractionCreateException, BadFieldValueException {
        Fraction[][] limitsMatrix = new Fraction[task.getLimitations()][];
        ArrayList<String> rowValues = new ArrayList<>();
        int row = 0;
        boolean nonZeroRow = false;
        for (Node node : matrix.getChildren()) {
            if (node instanceof TextField textField) {
                String value = textField.getText();
                if (value.isEmpty())
                    throw new BadFieldValueException("Пустое поле в матрице ограничений!");
                rowValues.add(value);
                if (!value.equals("0")) nonZeroRow = true;
                if (rowValues.size() == task.getVariables() + 1) {
                    if (!nonZeroRow)
                        throw new BadFieldValueException("Ограничение " + (row + 1) + " не может состоять из нолей!");
                    limitsMatrix[row] = new Fraction[rowValues.size()];
                    for (int i = 0; i < rowValues.size(); i++) {
                        String[] fractionParts = rowValues.get(i).split("/");
                        if (fractionParts.length == 1) {
                            limitsMatrix[row][i] = new Fraction(Integer.parseInt(fractionParts[0]), 1);
                        } else {
                            limitsMatrix[row][i] = new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
                        }
                    }
                    rowValues.clear();
                    nonZeroRow = false;
                    row++;
                }
            }
        }
        return limitsMatrix;
    }

    private ArrayList<Integer> readBases() throws BasesFormatException {
        ArrayList<Integer> bases = new ArrayList<>();
        for (Node node : matrixFun.getChildren()) {
            if (node instanceof CheckBox checkBox && checkBox.isSelected())
                bases.add(GridPane.getColumnIndex(checkBox));
        }
        if (bases.size() != task.getLimitations())
            throw new BasesFormatException("Кол-во базисных переменных должно быть равно кол-ву ограничений!");
        return bases;
    }

    @FXML
    protected void onBackToSettingsMenuClicked() {
        EnterTaskWindow window = new EnterTaskWindow();
        window.displayEnterSettings();
        currentStage.close();
    }

    @FXML
    protected void onBackToStartMenuClicked() {
        HelloWindow window = new HelloWindow();
        window.display();
        currentStage.close();
    }

    @FXML
    protected void onDownloadTaskMenuClicked() {
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
    void onReferenceMenuClicked() {
        MatrixReferenceWindow window = new MatrixReferenceWindow();
        window.display();
    }

    @FXML
    void onAboutMenuClicked() {
        AboutWindow window = new AboutWindow();
        window.display();
    }
}
