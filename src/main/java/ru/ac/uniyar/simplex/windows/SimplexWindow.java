package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.controllers.EnterTaskController;
import ru.ac.uniyar.simplex.controllers.SimplexController;
import ru.ac.uniyar.simplex.domain.TaskEntity;

import java.io.IOException;

public class SimplexWindow {

    private Stage primaryStage;

    public SimplexWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void display(TaskEntity task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("simplex-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Simplex - ввод настроек");
            primaryStage.setScene(scene);
            SimplexController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
