package ru.ac.uniyar.simplex.windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.controllers.HelloController;

import java.io.IOException;

public class HelloWindow {

    public void display() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloWindow.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Simplex");
            primaryStage.setScene(scene);
            HelloController controller = fxmlLoader.getController();
            controller.setProperties(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}