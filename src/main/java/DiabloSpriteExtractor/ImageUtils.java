package DiabloSpriteExtractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

public class ImageUtils {
	private static ClassLoader loader = ImageUtils.class.getClassLoader();
	
	public static BufferedImage loadImage(String filename) throws IOException, URISyntaxException {
		return loadImage(filename, "");
	}

	public static BufferedImage loadImage(String filename, String path) throws IOException {
		return ImageIO.read(loader.getResourceAsStream(String.format("%s", Paths.get(path, filename))));
	}
	
	public static BufferedImage loadImage2(String filename, String path) throws IOException, URISyntaxException {
		String fullName = String.format("%s", Paths.get(path, filename));
		byte[] bytes = IOUtils.toByteArray(loader.getResourceAsStream(fullName));
        int [] pixels = DDSReader.read(bytes, DDSReader.ARGB, 0);
        int width = DDSReader.getWidth(bytes);
        int height = DDSReader.getHeight(bytes);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
	}

	public static BufferedImage[] split(BufferedImage image, int rows, int cols) {
		return split(image, rows, cols, rows * cols, 0);
	}

	public static BufferedImage[] split(BufferedImage image, int rows, int cols, int limit, int maxWidth) {
		BufferedImage[] frames = new BufferedImage[limit];
		int width = image.getWidth() / cols;
		int height = image.getHeight() / rows;
		for (int row = 0; row < rows; row++) {
			for (int col = 0, rowIndex = row * cols + col; col < cols && rowIndex + col < limit; col++) {
				int x = col * width, y = row * height;
				frames[rowIndex + col] = image.getSubimage(x, y, Math.min(width, maxWidth), height);
			}
		}
		return frames;
	}

	public static String stripExtension(String filename) {
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	public static void writeImage(BufferedImage image, String name, String ext, String path) throws IOException {
		String filename = String.format("%s.%s", Paths.get(path, name), ext);
		ImageIO.write(image, ext, new File(filename));
	}

	public static void writeImages(BufferedImage[] frames, String name, String ext, String path) throws IOException {
		int digits = (int) (Math.floor(Math.log10(frames.length - 1)) + 1);
		String filenameFormat = String.format("%%s_%%0%dd", digits);
		for (int i = 0; i < frames.length; i++) {
			writeImage(frames[i], String.format(filenameFormat, name, i), ext, path);
		}
	}

	public static BufferedImage[] fitImages(BufferedImage[] frames, int width, int height) {
		BufferedImage[] resized = new BufferedImage[frames.length];
		for (int i = 0; i < frames.length; i++) {
			resized[i] = fitImage(frames[i], width, height);
		}
		return resized;
	}

	public static BufferedImage fitImage(BufferedImage frame, int width, int height) {
		BufferedImage copy = new BufferedImage(width, height, frame.getType());
		int xOffset = (copy.getWidth() - frame.getWidth()) / 2;
		int yOffset = (copy.getHeight() - frame.getHeight()) / 2;
		copy.getGraphics().drawImage(frame, xOffset, yOffset, null);
		return copy;
	}
}
