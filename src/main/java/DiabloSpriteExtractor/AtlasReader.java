package DiabloSpriteExtractor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.common.base.CaseFormat;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AtlasReader {
	private static ClassLoader loader = AtlasReader.class.getClassLoader();
	private static List<Asset> assetList;
	private static Map<String, BufferedImage> cachedAffixes;

	static {
		try {
			assetList = loadAssets("Assets.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		cachedAffixes = new HashMap<String, BufferedImage>();
	}

	public static void displayMap() {
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(assetList));
	}

	public static void splitImages(String name, String outputPath) throws IOException, URISyntaxException {
		String atlasFilename = name + "_atlas.txt";
		String imageFilename = name + ".png";

		new File(outputPath).mkdirs(); // Make directories.

		BufferedImage img = ImageUtils.loadImage(imageFilename);
		List<AtlasSprite> sprites = parse(atlasFilename);

		sprites.stream().forEach(sprite -> {
			try {
				extractSprite(img, sprite, outputPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private static Asset lookupAsset(AtlasSprite sprite) {
		return assetList.stream().filter(a -> a.getFilename() != null && a.getFilename().equals(sprite.getName())).findFirst().orElse(null);
	}

	private static void extractSprite(BufferedImage img, AtlasSprite sprite, String outputPath) throws IOException {
		Asset asset = lookupAsset(sprite);
		String name = asset != null ? asset.getName() : sprite.getName();
		String filename = Optional.ofNullable(name).orElse(sprite.getName());
		BufferedImage image = img.getSubimage(sprite.getPosAbs().getStartX(), sprite.getPosAbs().getStartY(),
				sprite.getPosAbs().getWidth(), sprite.getPosAbs().getHeight());

		if (asset == null) {
			cachedAffixes.put(sprite.getName(), image); // Cache it...
		} else {
			if (asset.getAffixes() != null && !asset.getAffixes().isEmpty()) {
				image = compositeImage(image, asset.getAffixes());
			}
		}

		ImageUtils.writeImage(image, filename, "png", outputPath);
	}

	private static BufferedImage compositeImage(BufferedImage image, List<String> affixes) {
		BufferedImage composite = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics g = (Graphics2D) composite.getGraphics();
		for (String affix : affixes) {
			String filename = Paths.get(affix).getFileName().toString();
			BufferedImage subImg = cachedAffixes.get(filename);
			int y = image.getHeight() - subImg.getHeight();
			g.drawImage(subImg, 0, y, null);
		}
		g.drawImage(image, 0, 0, null);
		return composite;
	}

	private static List<AtlasSprite> parse(String atlasFilename) {
		List<AtlasSprite> sprites = new ArrayList<AtlasSprite>();
		InputStream is = loader.getResourceAsStream(atlasFilename);
		Scanner scanner = new Scanner(is);

		scanner.nextLine(); // Skip the first line.

		while (scanner.hasNextLine()) {
			sprites.add(AtlasSprite.parseLine(scanner.nextLine()));
		}

		scanner.close();

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sprites;
	}

	public static List<String> lines(String filename) throws IOException {
		return FileUtils.readLines(new File(loader.getResource(filename).getFile()), "UTF-8");
	}

	@SuppressWarnings("unused")
	private static Map<String, String> loadText(String filename) throws IOException {
		return lines(filename).stream().map(s -> s.trim().split("\t")).collect(Collectors.toMap(a -> a[0], a -> a[1]));
	}

	private static List<Asset> loadAssets(String filename) throws IOException {
		@SuppressWarnings("serial")
		final Type listType = new TypeToken<List<Asset>>() {
		}.getType();
		try (Reader reader = new FileReader(loader.getResource(filename).getFile())) {
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static <E> List<E> loadJsonArray(String filename) throws IOException {
		@SuppressWarnings("serial")
		final Type listType = new TypeToken<List<E>>() {
		}.getType();
		try (Reader reader = new FileReader(loader.getResource(filename).getFile())) {
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static String normalizeName(String name) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name).replaceAll("[_]+", "_");
	}
}
