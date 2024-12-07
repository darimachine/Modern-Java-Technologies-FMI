import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try {
            FileSystemImageManager fsImageManager = new LocalFileSystemImageManager();

            BufferedImage image = fsImageManager.loadImage(new File("car.jpg"));

            ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
            BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

            ImageAlgorithm sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);
            BufferedImage edgeDetectedImage = sobelEdgeDetection.process(image);

            fsImageManager.saveImage(grayscaleImage, new File("car-grayscale.jpg"));
            fsImageManager.saveImage(edgeDetectedImage, new File("car-edge-detected.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}