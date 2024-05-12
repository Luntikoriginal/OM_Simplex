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
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloWindow.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Simplex");
            stage.setScene(scene);
            HelloController controller = fxmlLoader.getController();
            controller.setProperties(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}