package bg.sofia.uni.fmi.mjt.newsfeed.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class JsonFileSaver {

    public static void saveJsonToFile(String jsonString, String fileName) {
        try {
            Path filePath = Path.of(fileName);
            Files.writeString(filePath, jsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("An error occurred while saving the json to file", e);
        }
    }
}