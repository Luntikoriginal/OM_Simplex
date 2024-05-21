package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.controllers.SimplexController;
import ru.ac.uniyar.simplex.domain.TaskEntity;

import java.io.IOException;

public class SimplexWindow {

    public void display(TaskEntity task) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("simplex-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Simplex - решение");
            primaryStage.setScene(scene);
            SimplexController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage, task);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
