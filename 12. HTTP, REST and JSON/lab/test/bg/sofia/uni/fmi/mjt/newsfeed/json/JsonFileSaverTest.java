package bg.sofia.uni.fmi.mjt.newsfeed.json;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonFileSaverTest {
    private static final String TEST_FILE = "test_articles.json";

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    void testSaveJsonToFileSuccess() throws IOException {
        String json = """
            {
                "status": "ok",
                "totalResults": 1,
                "articles": [
                    {
                        "title": "Test Article",
                        "description": "This is a test description."
                    }
                ]
            }
        """;

        JsonFileSaver.saveJsonToFile(json, TEST_FILE);

        Path filePath = Path.of(TEST_FILE);
        assertTrue(Files.exists(filePath), "The file should exist");
        String savedContent = Files.readString(filePath);
        assertEquals(json.trim(), savedContent.trim(), "The content of the file should match the input JSON");
    }
    @Test
    void testSaveJsonToFileInvalidPath() {

        String json = "{ \"status\": \"ok\" }";
        String invalidFileName = "invalid_path/invalid_file.json";

        assertThrows(RuntimeException.class,() -> JsonFileSaver.saveJsonToFile(json, invalidFileName));
    }
}
