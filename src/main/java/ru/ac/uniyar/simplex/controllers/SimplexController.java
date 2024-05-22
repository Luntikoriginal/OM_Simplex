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
import ru.ac.uniyar.simplex.utils.FileUtils;
import ru.ac.uniyar.simplex.utils.GaussUtils;
import ru.ac.uniyar.simplex.utils.SimplexUtils;
import ru.ac.uniyar.simplex.windows.AboutWindow;
import ru.ac.uniyar.simplex.windows.HelloWindow;
import ru.ac.uniyar.simplex.windows.SimplexReferenceWindow;
import ru.ac.uniyar.simplex.windows.SimplexWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SimplexController {

    private Stage currentStage;
    private ArrayList<SimplexEntity> steps;
    private int currentStep;
    private TaskEntity task;
    private Point2D selectedField;
    private Button lastSelectedButton;

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
            if (task.getSolutionWay().equals("auto")) {
                while (!checkAnswer(steps.get(currentStep))) nextStep();
            }
            initialize(steps.get(currentStep));
        } catch (Exception e) {
            welcomeText.setTextFill(Color.RED);
            welcomeText.setText(e.getMessage());
        }
    }

    private SimplexEntity createSE(TaskEntity task) throws FractionCreateException {
        Fraction[][] gMatrix = GaussUtils.gauss(task.getMatrix(), task.getBases());
        System.out.println("Результат Гаусса: " + Arrays.deepToString(gMatrix));
        return new SimplexEntity(task, gMatrix);
    }

    private void initialize(SimplexEntity simplex) {
        try {
            welcomeText.setText("Симплекс метод - шаг " + currentStep);
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

            checkAnswer(simplex);

            lastButton.setFocusTraversable(false);
            lastButton.setDisable(currentStep == 0);
            nextButton.setDisable(simplex.getPF().isEmpty());
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
            Fraction answerF = simplex.getST()[simplex.getBV().size()][simplex.getFV().size()].multiply(-1);
            answer.setText("Ответ: " + answerF.toString());
            return true;
        }
        return false;
    }

    @FXML
    protected void nextStep() throws FractionCreateException {
        SimplexEntity currentTable = steps.get(currentStep);
        if (selectedField == null) selectedField = currentTable.getPF().get(0);
        SimplexEntity nextTable = SimplexUtils.step(currentTable, selectedField);
        steps.add(nextTable);
        currentStep++;
        initialize(nextTable);
    }

    @FXML
    protected void lastStep() {
        SimplexEntity lastTable = steps.get(currentStep - 1);
        currentStep--;
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
        } catch (IOException e) {
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
