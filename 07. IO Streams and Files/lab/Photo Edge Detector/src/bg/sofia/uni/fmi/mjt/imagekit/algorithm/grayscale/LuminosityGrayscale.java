package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    static final double RED_PARAM = 0.21;
    static final double GREEN_PARAM = 0.72;
    static final double BLUE_PARAM = 0.07;

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Null Image");
        }
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        BufferedImage grayScaleImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                Color color = new Color(image.getRGB(i, j));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int gray = (int) ((RED_PARAM * red) + (GREEN_PARAM * green) + (BLUE_PARAM * blue));
                Color newColor = new Color(gray, gray, gray);
                grayScaleImage.setRGB(i, j, newColor.getRGB());
            }
        }
        return grayScaleImage;
    }
}
