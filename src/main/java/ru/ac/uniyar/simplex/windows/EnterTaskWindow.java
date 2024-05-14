package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.controllers.EnterMatrixController;
import ru.ac.uniyar.simplex.controllers.EnterTaskController;
import ru.ac.uniyar.simplex.domain.TaskEntity;

import java.io.IOException;

public class EnterTaskWindow {

    private final Stage primaryStage;

    private final Stage currentStage;

    public EnterTaskWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        currentStage = new Stage();
    }

    public EnterTaskWindow(Stage primaryStage, Stage currentStage) {
        this.primaryStage = primaryStage;
        this.currentStage = currentStage;
    }

    public void displayEnterSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter-settings-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setTitle("Simplex - ввод настроек");
            currentStage.setScene(scene);
            EnterTaskController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage, currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayEnterMatrix(TaskEntity task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter-matrix-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setTitle("Simplex - ввод матрицы");
            currentStage.setScene(scene);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            double centerXPosition = bounds.getMinX() + (bounds.getWidth() - currentStage.getWidth()) / 2;
            double centerYPosition = bounds.getMinY() + (bounds.getHeight() - currentStage.getHeight()) / 2;

            currentStage.setX(centerXPosition);
            currentStage.setY(centerYPosition);

            EnterMatrixController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage, currentStage, task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
