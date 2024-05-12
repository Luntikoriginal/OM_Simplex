package ru.ac.uniyar.simplex;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.windows.HelloWindow;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HelloWindow window = new HelloWindow();
        window.display();
    }
}
