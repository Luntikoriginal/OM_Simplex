package ru.ac.uniyar.simplex.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.Fraction;
import ru.ac.uniyar.simplex.domain.SimplexEntity;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import ru.ac.uniyar.simplex.exceptions.FractionCreateException;
import ru.ac.uniyar.simplex.utils.GaussUtils;
import ru.ac.uniyar.simplex.utils.SimplexUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplexController {

    private Stage primaryStage;

    private ArrayList<SimplexEntity> steps;

    private int currentStep;

    private TaskEntity task;

    public void setProperties(Stage primaryStage, TaskEntity task) {
        this.primaryStage = primaryStage;
        this.task = task;
        this.currentStep = 0;
        this.steps = new ArrayList<>();
        createST(task);
        initialize();
    }

    @FXML
    private Label welcomeText;

    private void createST(TaskEntity task) {
        try {
            Fraction[][] gMatrix = GaussUtils.gauss(task.getMatrix(), task.getBases());
            System.out.println("Результат Гаусса: " + Arrays.deepToString(gMatrix));
            SimplexEntity simplex = new SimplexEntity(task, gMatrix);
            System.out.println("Результат преобразования в симплекс:");
            System.out.println(Arrays.deepToString(simplex.getST()));
            System.out.println(simplex.getBase());
            steps.add(simplex);
        } catch (Exception e) {
            welcomeText.setTextFill(Color.RED);
            welcomeText.setText(e.getMessage());
        }
    }

    private void initialize() {

    }
}
