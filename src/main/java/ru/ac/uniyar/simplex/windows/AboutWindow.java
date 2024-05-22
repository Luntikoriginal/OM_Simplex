package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutWindow {

    public void display() {
        try {
            Stage secondaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloWindow.class.getResource("about-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            secondaryStage.setTitle("Simplex - о приложении");
            secondaryStage.setScene(scene);
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
