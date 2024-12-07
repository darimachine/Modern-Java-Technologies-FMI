package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SobelEdgeDetectionTest {
    private EdgeDetectionAlgorithm algorithm;
    private BufferedImage image;
    @BeforeEach
    void setUp() {
        algorithm = new SobelEdgeDetection(new LuminosityGrayscale());
        try {
            image = ImageIO.read(new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kitten.png"));
        } catch (IOException e) {
            fail("IoException not expectedted on reading");
        }

    }
    @Test
    void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> algorithm.process(null), "It should throw IllegalArgumentException when the image is null");
    }

    @Test
    void testProcessWithValidImage() {
        BufferedImage edgeImage = algorithm.process(image);
        assertNotNull(edgeImage, "Processed edge-detected image should not be null");
        assertEquals(image.getWidth(), edgeImage.getWidth(), "Width of the edge-detected image should match the original image");
        assertEquals(image.getHeight(), edgeImage.getHeight(), "Height of the edge-detected image should match the original image");

        boolean hasDifferentPixels = false;
        for (int i = 1; i < edgeImage.getWidth() - 1; i++) {
            for (int j = 1; j < edgeImage.getHeight() - 1; j++) {
                if (image.getRGB(i, j) != edgeImage.getRGB(i, j)) {
                    hasDifferentPixels = true;
                    break;
                }
            }
        }
        assertTrue(hasDifferentPixels, "The edge-detected image should have different pixel values compared to the original image");
    }
}
