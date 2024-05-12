package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.controllers.EnterMatrixController;
import ru.ac.uniyar.simplex.domain.TaskEntity;

import java.io.IOException;

public class EnterMatrixWindow {

    private Stage primaryStage;

    private Stage currentStage;

    private TaskEntity task;

    public EnterMatrixWindow(Stage primaryStage, Stage currentStage, TaskEntity task) {
        this.primaryStage = primaryStage;
        this.currentStage = currentStage;
        this.task = task;
    }

    public void display() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter-matrix-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setTitle("Simplex - ввод матрицы");
            currentStage.setScene(scene);
            // Получаем размер экрана
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            // Вычисляем позицию окна по центру экрана
            double centerXPosition = bounds.getMinX() + (bounds.getWidth() - currentStage.getWidth()) / 2;
            double centerYPosition = bounds.getMinY() + (bounds.getHeight() - currentStage.getHeight()) / 2;

            // Устанавливаем позицию окна по центру экрана
            currentStage.setX(centerXPosition);
            currentStage.setY(centerYPosition);
            EnterMatrixController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage, currentStage, task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
