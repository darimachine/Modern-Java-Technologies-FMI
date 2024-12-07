package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class LuminosityGrayscaleTest {

    private BufferedImage image;
    private GrayscaleAlgorithm algorithm;
    @BeforeEach
    void setUp()
    {
        algorithm= new LuminosityGrayscale();
        try {
            image = ImageIO.read(new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kitten.png"));
        } catch (IOException e) {
            fail("IoException not expectedted on reading");
        }

    }
    @Test
    void testProccessWithNullImage() {
        assertThrows(IllegalArgumentException.class,()->algorithm.process(null),
            "It should throw IllegalArgumentException for NUll");
    }
    @Test
    void testProcessWithValidImage() {
        BufferedImage grayImage = algorithm.process(image);
        assertNotNull(grayImage, "Processed grayscale image should not be null");
        assertEquals(image.getWidth(), grayImage.getWidth(), "Width of the grayscale image should match the original image");
        assertEquals(image.getHeight(), grayImage.getHeight(), "Height of the grayscale image should match the original image");

        // Verify that each pixel is converted to grayscale
        for (int i = 0; i < grayImage.getWidth(); i++) {
            for (int j = 0; j < grayImage.getHeight(); j++) {
                Color color = new Color(grayImage.getRGB(i, j));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                assertEquals(red, green, "Red and green components should be equal in grayscale image");
                assertEquals(green, blue, "Green and blue components should be equal in grayscale image");
            }
        }
    }
}
