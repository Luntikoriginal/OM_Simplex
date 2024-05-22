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

    public EnterTaskWindow() {
        primaryStage = new Stage();
    }

    public EnterTaskWindow(Stage currentStage) {
        this.primaryStage = currentStage;
    }

    public void displayEnterSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter-settings-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Simplex - ввод настроек");
            primaryStage.setScene(scene);
            EnterTaskController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayEnterMatrix(TaskEntity task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter-matrix-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Simplex - ввод матрицы");
            primaryStage.setScene(scene);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            double centerXPosition = bounds.getMinX() + (bounds.getWidth() - primaryStage.getWidth()) / 2;
            double centerYPosition = bounds.getMinY() + (bounds.getHeight() - primaryStage.getHeight()) / 2;

            primaryStage.setX(centerXPosition);
            primaryStage.setY(centerYPosition);

            EnterMatrixController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage, task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
