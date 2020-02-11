import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.GsonBuilder;

import DiabloSpriteExtractor.Asset;
import DiabloSpriteExtractor.AtlasReader;

public class LangCompare {
	private static Map<String, String> exceptions;
	
	static {
		exceptions = new HashMap<String, String>();
		//exceptions.put("p67_Cosmetic_PortraitFrame_Season19", "Cosmetic_PortraitFrame_Valor");
	}
	
	public static void main(String[] args) {
		try {
			createAssetFile("Items.txt", "Filenames.txt", "out/Assets.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createAssetFile(String itemsFilename, String filenameList, String assetFilename) throws IOException {
		List<String> items = AtlasReader.lines(itemsFilename);
		List<String> filenames = AtlasReader.lines(filenameList);
		List<Asset> results = joinKeysWithFilenames(items, filenames);
		writeFile(results, assetFilename);
		System.out.println("File written...");
	}

	public static List<Asset> joinKeysWithFilenames(List<String> items, List<String> filenames) {
		List<Asset> assets = new ArrayList<Asset>(); 
		
		for (String filename : filenames) {
			Asset asset = new Asset();
			asset.setFilename(filename);
			asset.setName(findClosestMatch(filename, items, item -> item.split("\t")[0]).split("\t")[1]);
			assets.add(asset);
		}

		return assets;
	}

	public static <E> void writeLinesToFile(List<E> lines, String filename) throws IOException {
		FileWriter writer = new FileWriter("out/Names.txt"); 
		for(E line: lines) {
		  writer.write(line + System.lineSeparator());
		}
		writer.close();
	}

	public static void writeFile(Object obj, String filename) throws IOException {
		FileWriter writer = new FileWriter(filename); 
		writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
		writer.close();
	}

	public static String findClosestMatch(String str, List<String> terms, Function<String, String> termFn) {
		if (exceptions.containsKey(str)) {
			String key = exceptions.get(str);
			return terms.stream().filter(term -> term.startsWith(key)).findFirst().get();
		}

		double highestRating = 0;
		String closestMatch = null;
		for (String term : terms) {
			double dist = StringSimilarity.similarity(str, termFn.apply(term));
			if (dist > highestRating) {
				highestRating = dist;
				closestMatch = term;
			}
		}
		return closestMatch;
	}
}
