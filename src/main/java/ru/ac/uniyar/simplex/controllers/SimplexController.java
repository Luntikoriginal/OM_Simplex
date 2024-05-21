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
import ru.ac.uniyar.simplex.utils.GaussUtils;
import ru.ac.uniyar.simplex.utils.SimplexUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SimplexController {

    private Stage primaryStage;
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

    public void setProperties(Stage primaryStage, TaskEntity task) {
        try {
            this.primaryStage = primaryStage;
            this.task = task;
            this.currentStep = 0;
            this.steps = new ArrayList<>();
            SimplexEntity simplex = createSE(task);
            steps.add(simplex);
            initialize(simplex);
        } catch (Exception e) {
            welcomeText.setTextFill(Color.RED);
            welcomeText.setText(e.getMessage());
        }
    }

    private SimplexEntity createSE(TaskEntity task) throws FractionCreateException {
        Fraction[][] gMatrix = GaussUtils.gauss(task.getMatrix(), task.getBases());
        System.out.println("Результат Гаусса: " + Arrays.deepToString(gMatrix));
        SimplexEntity simplex = new SimplexEntity(task, gMatrix);
        System.out.println("Результат преобразования в симплекс:");
        System.out.println(Arrays.deepToString(simplex.getST()));
        System.out.println(simplex.getBase());
        System.out.println(simplex.getPF());
        return simplex;
    }

    private void initialize(SimplexEntity simplex) {
        welcomeText.setText("Симплекс метод - шаг " + currentStep);
        table.getChildren().clear();

        StringBuilder builder = new StringBuilder("Базис: < ");
        for (Map.Entry<Integer, Fraction> entry : simplex.getBase().entrySet()) {
            builder.append(entry.getValue());
            if (entry.getKey() != simplex.getBase().size())
                builder.append(", ");
        }
        builder.append(" >");
        base.setText(builder.toString());

        // header
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

        // text fields
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

    @FXML
    private void nextStep() throws FractionCreateException {
        SimplexEntity nextTable = SimplexUtils.step(steps.get(currentStep), selectedField);
        steps.add(nextTable);
        currentStep++;
        initialize(nextTable);
    }
}
