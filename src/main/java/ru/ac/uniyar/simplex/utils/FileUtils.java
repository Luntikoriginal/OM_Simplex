package ru.ac.uniyar.simplex.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.ac.uniyar.simplex.domain.TaskEntity;
import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void saveTaskToJSONFile(TaskEntity task) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File("task.json"), task);
    }

    public static TaskEntity readTaskFromJSON() throws IOException {
        File file = new File("task.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, TaskEntity.class);
    }
}
