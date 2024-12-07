package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class LocalFileSystemImageManagerTest {
    private static FileSystemImageManager system;
    private static File validFile;
    private static File invalidFile;
    private static File unsupported;
    private static BufferedImage image;

    @BeforeAll
    static void setUp() {
        validFile = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kitten.png");
        invalidFile = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/randoim.jpg");
        unsupported = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kitten.amg");
        system = new LocalFileSystemImageManager();
        try {
            image = system.loadImage(validFile);
        } catch (IOException e) {
            fail("validFile parametr invalid");
        }
    }

    @Test
    void testLoadImageWithNull() {
        assertThrows(IllegalArgumentException.class, () -> system.loadImage(null), "It Should throw null exception");
    }

    @Test
    void testLoadImageWithInvalidFile() {
        invalidFile = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/randoim.jpg");
        assertThrows(IOException.class, () -> system.loadImage(invalidFile),
            "It Should throw InvalidFile exception");
    }

    @Test
    void testLoadImageWithUnSupportedFile() {
        unsupported = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kitten.amg");
        assertThrows(IOException.class, () -> system.loadImage(unsupported),
            "It Should throw exception unsupported file");
    }

    @Test
    void testLoadImageWithValidFile() {
        validFile = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kitten.png");
        assertNotNull(image, "image should be loaded succesfully");
    }

    @Test
    void testLoadImagesFromDirectoryWithNull() {
        assertThrows(IllegalArgumentException.class, () -> system.loadImagesFromDirectory(null),
            "It should throw IllegalArgumentException when directory is null");
    }

    @Test
    void testLoadImagesFromNonExistentDirectory() {
        File invalidDirectory = new File("test/resources/invalid-directory");
        assertThrows(IOException.class, () -> system.loadImagesFromDirectory(invalidDirectory),
            "It should throw IOException when the directory does not exist");
    }

    @Test
    void testLoadImagesFromDirectoryWithUnsupportedFile() {
        File directoryWithUnsupportedFile = new File(new File(".").getAbsolutePath());
        assertThrows(IOException.class, () -> system.loadImagesFromDirectory(directoryWithUnsupportedFile),
            "It should throw IOException when the directory contains unsupported file format");
    }

    @Test
    void testLoadImagesFromValidDirectory() {
        File validDirectory = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources");
        try {
            List<BufferedImage> images = system.loadImagesFromDirectory(validDirectory);
            assertNotNull(images, "The list of images should not be null");
            assertFalse(images.isEmpty(), "The list of images should not be empty");
        } catch (IOException e) {
            fail("IOException should not be thrown for a valid directory");
        }
    }

    @Test
    void testSaveImageWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> system.saveImage(null, validFile));
    }

    @Test
    void testSaveImageWithNullImageFile() {
        assertThrows(IllegalArgumentException.class, () -> system.saveImage(image, null));

    }

    @Test
    void testSaveImageWithImageFileDoesntExist() {
        assertThrows(IOException.class, () -> system.saveImage(image, invalidFile));
    }

    @Test
    void testSaveImageWithImageFileUnsupported() {
        assertThrows(IOException.class, () -> system.saveImage(image, unsupported));

    }

    @Test
    void testSaveImageWithExistingFile() {
        assertThrows(IOException.class, () -> system.saveImage(image, validFile));
    }

    @Test
    void testSaveImageSuccesfully() {
        File newFile = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kotka2.png");
        if (newFile.exists()) {
            newFile.delete();
        }
        assertDoesNotThrow(() -> system.saveImage(image, newFile), "It should save the image succesfully");
        assertTrue(newFile.exists(), "Output file should be created");

    }

    @Test
    void testSaveImageIncorrectFormat() {
        File newFile = new File("test/bg/sofia/uni/fmi/mjt/imagekit/resources/kotka2.pngu");
        assertThrows(IOException.class, () -> system.saveImage(image, newFile),
            "Unsupported Type to save");

    }

}
