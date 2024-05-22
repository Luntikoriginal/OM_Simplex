package ru.ac.uniyar.simplex.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.ac.uniyar.simplex.domain.TaskEntity;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void saveTaskToJSONFile(TaskEntity task) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(file, task);
        }
    }

    public static TaskEntity readTaskFromJSON() throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, TaskEntity.class);
        }
        return null;
    }
}
