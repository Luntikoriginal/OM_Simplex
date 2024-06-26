package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.SimplexEntity;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;
import ru.ac.uniyar.simplex.utils.ArtificialBasesUtils;
import ru.ac.uniyar.simplex.utils.FileUtils;
import ru.ac.uniyar.simplex.utils.GaussUtils;
import ru.ac.uniyar.simplex.utils.SimplexUtils;
import ru.ac.uniyar.simplex.windows.AboutWindow;
import ru.ac.uniyar.simplex.windows.HelloWindow;
import ru.ac.uniyar.simplex.windows.SimplexReferenceWindow;
import ru.ac.uniyar.simplex.windows.SimplexWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SimplexController {

    private Stage currentStage;
    private ArrayList<SimplexEntity> steps;
    private int currentStep;
    private int lastABStep;
    private TaskEntity task;
    private Point2D selectedField;
    private Button lastSelectedButton;
    private String solutionType;

    @FXML
    private Label welcomeText;
    @FXML
    private Label base;
    @FXML
    private GridPane table;
    @FXML
    private Label answer;
    @FXML
    private Button lastButton;
    @FXML
    private Button nextButton;

    public void setProperties(Stage primaryStage, TaskEntity task) {
        try {
            this.currentStage = primaryStage;
            this.task = task;
            this.currentStep = 0;
            this.steps = new ArrayList<>();
            SimplexEntity simplex = createSE(task);
            steps.add(simplex);
            adjustWindowSize(simplex);
            if (task.getSolutionWay().equals("auto")) {
                while (checkAB(steps.get(currentStep)).equals("continue")) nextStep();
                if (checkAB(steps.get(currentStep)).equals("simplex")) {
                    lastABStep = currentStep;
                    nextStep();
                    while (!checkAnswer(steps.get(currentStep))) nextStep();
                }
            }
            initialize(steps.get(currentStep));
        } catch (Exception e) {
            welcomeText.setTextFill(Color.RED);
            welcomeText.setText(e.getMessage());
        }
    }

    private SimplexEntity createSE(TaskEntity task) throws FractionCreateException {
        if (task.getSolutionType().equals("simplex")) {
            solutionType = "simplex";
            Fraction[][] gMatrix = GaussUtils.gauss(task.getMatrix(), task.getBases());
            return new SimplexEntity(task, gMatrix);
        } else {
            solutionType = "artificial basis";
            return new SimplexEntity(task);
        }
    }

    private void adjustWindowSize(SimplexEntity simplex) {
        int fieldWith = 60;
        int fieldHeight = 35;

        double newWidth = fieldWith * (simplex.getFV().size() + 2) + 150;
        double newHeight = fieldHeight * (simplex.getBV().size() + 5) + 130;

        currentStage.setMinWidth(newWidth);
        currentStage.setMinHeight(newHeight);
        currentStage.setWidth(newWidth);
        currentStage.setHeight(newHeight);
        currentStage.centerOnScreen();
    }


    private void initialize(SimplexEntity simplex) {
        try {
            if (solutionType.equals("simplex"))
                welcomeText.setText("Симплекс метод - шаг " + currentStep);
            else
                welcomeText.setText("Метод искусственных базисов - шаг " + currentStep);
            answer.setText("");
            table.getChildren().clear();

            StringBuilder builder = new StringBuilder("Базис: < ");
            for (Map.Entry<Integer, Fraction> entry : simplex.getBase().entrySet()) {
                builder.append(entry.getValue());
                if (entry.getKey() != simplex.getBase().size())
                    builder.append(", ");
            }
            builder.append(" >");
            base.setText(builder.toString());

            printHeader(simplex);

            printBody(simplex);

            if (solutionType.equals("simplex")) checkAnswer(simplex);
            else checkAB(simplex);

            lastButton.setFocusTraversable(false);
            lastButton.setDisable(currentStep == 0);
            nextButton.setDisable(simplex.getPF().isEmpty() && solutionType.equals("simplex"));
        } catch (Exception e) {
            welcomeText.setText(e.getMessage());
            welcomeText.setTextFill(Color.RED);
        }
    }

    private void printBody(SimplexEntity simplex) {
        for (int i = 1; i <= simplex.getBV().size() + 1; i++) {
            String rowHeader;
            if (i != simplex.getBV().size() + 1) rowHeader = "x" + simplex.getBV().get(i - 1);
            else rowHeader = "P(x)";
            Label rowLabel = new Label(rowHeader);
            rowLabel.setFont(Font.font(14));
            table.add(rowLabel, 0, i);
            GridPane.setHalignment(rowLabel, HPos.CENTER);

            for (int j = 1; j <= simplex.getFV().size() + 1; j++) {
                Button button = new Button(simplex.getST()[i - 1][j - 1].toString());
                button.setFocusTraversable(false);
                button.setPrefWidth(50);

                if (i != simplex.getBV().size() + 1 || j != simplex.getFV().size() + 1)
                    button.setStyle("-fx-background-color: lightgrey;");
                else button.setStyle("-fx-background-color: lightpink;");

                if (simplex.containsPoint(i - 1, j - 1)) {
                    button.setStyle("-fx-background-color: lightblue;");
                    button.setOnMouseClicked(event -> {
                        int r = GridPane.getRowIndex(button) - 1;
                        int s = GridPane.getColumnIndex(button) - 1;
                        selectedField = new Point2D(r, s);
                        button.setStyle("-fx-background-color: lightgreen;");
                        if (lastSelectedButton != null) lastSelectedButton.setStyle("-fx-background-color: lightblue;");
                        lastSelectedButton = button;
                    });
                }
                table.add(button, j, i);
            }
        }
    }

    private void printHeader(SimplexEntity simplex) {
        for (int i = 0; i <= simplex.getFV().size() + 1; i++) {
            String columnHeader;
            if (i == 0) columnHeader = "X(" + currentStep + ")";
            else if (i == simplex.getFV().size() + 1) columnHeader = "b";
            else columnHeader = "x" + simplex.getFV().get(i - 1);
            Label columnLabel = new Label(columnHeader);
            columnLabel.setFont(Font.font(14));
            table.add(columnLabel, i, 0);
            GridPane.setHalignment(columnLabel, HPos.CENTER);
        }
    }

    private boolean checkAnswer(SimplexEntity simplex) throws FractionCreateException {
        boolean notNegativeColumn;
        for (int j = 0; j < simplex.getST()[0].length; j++) {
            notNegativeColumn = false;

            for (int i = 0; i < simplex.getST().length; i++) {
                if (i != simplex.getST().length - 1) {
                    if (simplex.getST()[i][j].getNumerator() > 0) {
                        notNegativeColumn = true;
                    }
                } else {
                    if (simplex.getST()[i][j].getNumerator() >= 0) {
                        notNegativeColumn = true;
                    }
                }
            }

            if (!notNegativeColumn) {
                answer.setText("Ответ: не ограничена");
                return true;
            }
        }
        if (simplex.getPF().isEmpty()) {
            Fraction answerF = simplex.getST()[simplex.getBV().size()][simplex.getFV().size()];
            if (task.getTaskType().equals("min"))
                answerF = answerF.multiply(-1);
            answer.setText("Ответ: " + answerF.toString());
            return true;
        }
        return false;
    }

    private String checkAB(SimplexEntity simplex) {
        if (simplex.getPF().isEmpty()) {
            Fraction[][] simplexTable = simplex.getST();

            int rows = simplexTable.length;
            int cols = simplexTable[0].length;

            for (int j = 0; j < cols; j++) {
                if (!(simplexTable[rows - 1][j].getNumerator() == 0)) {
                    answer.setText("Ответ: система несовместна");
                    return "stop";
                }
            }
            return "simplex";
        }
        return "continue";
    }

    @FXML
    protected void nextStep() throws FractionCreateException {
        SimplexEntity currentTable = steps.get(currentStep);
        if (currentTable.getPF().isEmpty()) {
            for (int i = task.getVariables() + 1; i <= task.getVariables() + task.getLimitations(); i++) {
                if (currentTable.getBV().contains(i)) {
                    Point2D dummy = new Point2D(currentTable.getBV().indexOf(i), 0);
                    currentTable = SimplexUtils.step(currentTable, dummy);
                    ArtificialBasesUtils.deleteColumn(currentTable, (int) dummy.getY());
                }
            }
            solutionType = "simplex";
            lastABStep = currentStep;
            SimplexEntity nextTable = new SimplexEntity(currentTable);
            if (task.getTaskType().equals("max")) {
                Fraction[] convertedFunc = SimplexUtils.convertFunc(task.getFunction());
                SimplexUtils.solveFunc(convertedFunc, nextTable, nextTable.getST());
            } else SimplexUtils.solveFunc(task.getFunction(), nextTable, nextTable.getST());
            SimplexUtils.findPossibleFields(nextTable);
            steps.add(nextTable);
            currentStep++;
            selectedField = null;
            initialize(nextTable);
        } else {
            if (selectedField == null) selectedField = currentTable.getPF().get(0);
            SimplexEntity nextTable = SimplexUtils.step(currentTable, selectedField);
            if (solutionType.equals("artificial basis") && nextTable.getFV().get((int) selectedField.getY()) > task.getVariables())
                ArtificialBasesUtils.deleteColumn(nextTable, (int) selectedField.getY());
            steps.add(nextTable);
            currentStep++;
            selectedField = null;
            initialize(nextTable);
        }
    }

    @FXML
    protected void lastStep() {
        steps.remove(currentStep);
        SimplexEntity lastTable = steps.get(currentStep - 1);
        currentStep--;
        if (currentStep == lastABStep)
            solutionType = "artificial basis";
        initialize(lastTable);
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
        } catch (Exception e) {
            welcomeText.setText("Не удалось прочитать файл!");
            welcomeText.setTextFill(Color.RED);
        }
    }

    @FXML
    protected void onSaveTaskMenuClicked() {
        try {
            FileUtils.saveTaskToJSONFile(task);
        } catch (IOException e) {
            welcomeText.setText("Не удалось сохранить задачу в файл!");
            welcomeText.setTextFill(Color.RED);
        }
    }

    @FXML
    void onReferenceMenuClicked() {
        SimplexReferenceWindow window = new SimplexReferenceWindow();
        window.display();
    }

    @FXML
    void onAboutMenuClicked() {
        AboutWindow window = new AboutWindow();
        window.display();
    }
}
