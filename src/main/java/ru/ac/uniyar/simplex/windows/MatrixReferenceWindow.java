package ru.ac.uniyar.simplex.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MatrixReferenceWindow {

    public void display() {
        try {
            Stage secondaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloWindow.class.getResource("matrix-reference-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            secondaryStage.setTitle("Ввод матрицы - справка");
            secondaryStage.setScene(scene);
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
