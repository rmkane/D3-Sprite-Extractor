package DiabloSpriteExtractor;

import java.nio.file.Paths;
import java.util.List;

public class Asset {
	private String name;
	private String key;
	private String path;
	private String filename;
	private String description;
	private List<String> affixes;

	public Asset() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAffixes() {
		return affixes;
	}

	public void setAffixes(List<String> affixes) {
		this.affixes = affixes;
	}

	public String getFullFilename() {
		return String.format("%s.png", Paths.get(path, filename));
	}

	@Override
	public String toString() {
		return String.format("Asset [name=%s, filename=%s]", name, filename);
	}
}
