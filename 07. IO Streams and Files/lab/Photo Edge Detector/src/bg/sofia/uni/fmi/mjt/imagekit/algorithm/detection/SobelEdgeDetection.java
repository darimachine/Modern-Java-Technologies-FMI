package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    static final int[][] SOBELX = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };
    static final int[][] SOBELY = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };
    ImageAlgorithm algorithm;
    static final int PIXEL_MAX_SIZE = 255;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.algorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image is null");
        }
        BufferedImage processedImage = algorithm.process(image);
        int width = processedImage.getWidth();
        int height = processedImage.getHeight();
        BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                int gradientX = 0;
                int gradientY = 0;
                for (int kernelRow = 0; kernelRow < SOBELX.length; kernelRow++) {
                    for (int kernelCol = 0; kernelCol < SOBELY.length; kernelCol++) {
                        int neighborX = col + (kernelCol - 1);
                        int neighborY = row + (kernelRow - 1);
                        Color color = new Color(processedImage.getRGB(neighborX, neighborY));
                        int intensity = color.getRed();
                        gradientX += SOBELX[kernelRow][kernelCol] * intensity;
                        gradientY += SOBELY[kernelRow][kernelCol] * intensity;
                    }
                }
                int magnitude = (int) Math.min(PIXEL_MAX_SIZE,
                    Math.sqrt(gradientX * gradientX + gradientY * gradientY));
                Color edgeColor = new Color(magnitude, magnitude, magnitude);
                imageResult.setRGB(col, row, edgeColor.getRGB());
            }
        }
        return imageResult;
    }
}
