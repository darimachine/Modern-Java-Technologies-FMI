package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("File is null");
        }

        if (!imageFile.exists() || !imageFile.isFile() || !isCorrectFormat(imageFile)) {
            throw new IOException("File is not file or doesnt exist or its not on correct format");
        }
        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {

        if (imagesDirectory == null) {
            throw new IllegalArgumentException("File is null");
        }
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("File is not directory or doesnt exist or its not on correct format");
        }
        List<BufferedImage> images = new ArrayList<>();
        for (File file : imagesDirectory.listFiles()) {
            if (file.isFile() && isCorrectFormat(file)) {
                images.add(ImageIO.read(file));
            } else if (!isCorrectFormat(file)) {
                throw new IOException("Directory contains unsupported file format");
            }
        }
        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("image is null");
        }
        File parentDirectory = imageFile.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            throw new IOException("The parent directory does not exist.");
        }
        if (imageFile.exists()) {
            throw new IOException("Unsoported Format or file exists");
        }
        String format;
        String fileName = imageFile.getName();
        if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            format = "jpg";
        } else if (fileName.endsWith(".png")) {
            format = "png";
        } else if (fileName.endsWith(".bmp")) {
            format = "bmp";
        } else {
            throw new IOException("Unsupported format");
        }
        boolean isSaved = ImageIO.write(image, format, imageFile);
        if (!isSaved) {
            throw new IOException("File not saved correctly");
        }
    }

    private boolean isCorrectFormat(File fileImage) {
        String image = fileImage.getName();
        return image.endsWith(".jpg") || image.endsWith(".jpeg") || image.endsWith(".png") || image.endsWith(".bmp");
    }
}
