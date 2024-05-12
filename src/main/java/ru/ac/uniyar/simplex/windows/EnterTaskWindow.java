package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.controllers.EnterMatrixController;
import ru.ac.uniyar.simplex.controllers.EnterTaskController;

import java.io.IOException;

public class EnterTaskWindow {

    private final Stage primaryStage;

    public EnterTaskWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void display() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter-task-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Simplex - ввод настроек");
            stage.setScene(scene);
            EnterTaskController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage, stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
