import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import DiabloSpriteExtractor.AtlasReader;

public class Driver {
	public static final String OUTPUT_DIR = "out";

	public static void main(String[] args) {
		AtlasReader.displayMap();
		
		List<String> spritesheets = Arrays.asList(
			//"items/2DInventoryItem",
			//"items/2DInventoryItem_p",
			//"items/2DInventoryItem_x1",
			"runes/2DInventoryRunes",
			"runes/2DInventoryRunes_A"
			//"sets/2DInventoryBoots_p",
			//"sets/2DInventoryChestArmor_p",
			//"sets/2DInventoryGloves_p",
			//"sets/2DInventoryHelms_p",
			//"sets/2DInventoryPants_p",
			//"sets/2DInventoryShoulders_p",
			//"portraits/2DUIPortraits",
			//"portraits/2DUIPortraits_b",
			//"portraits/2DUIPortraits_c",
			//"portraits/2DUIPortraits_CheatDeath",
			//"portraits/2DUIPortraits_p1",
			//"portraits/2DUIPortraits_p2",
			//"portraits/2DUIPortraits_p6",
			//"portraits/2DUIPortraits_PVP",
			//"prestige/2DUIPrestige",
			//"prestige/2DUIPrestige_Cosmetic_P2",
			//"prestige/2DUIPrestige_Cosmetic_p7"
		);
		
		spritesheets.stream().forEach(spritesheet -> {
			try {
				String outputPath = Paths.get(OUTPUT_DIR, spritesheet).toString();
				AtlasReader.splitImages(spritesheet, outputPath);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		});

		System.out.println("Finished!");
	}
}
