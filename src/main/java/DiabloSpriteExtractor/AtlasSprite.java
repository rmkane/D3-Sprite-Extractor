package DiabloSpriteExtractor;

public class AtlasSprite {
	private String name;
	private Position.Double2D posPct;
	private Position.Integer2D posAbs;

	public static AtlasSprite parseLine(String line) throws NumberFormatException {
		if (line == null || line.isEmpty()) {
			throw new IllegalArgumentException("Line is empty!");
		}

		String[] tokens = line.split("\t");

		if (tokens.length != 9) {
			throw new IllegalArgumentException("Unparseable line: " + line);
		}

		String name = tokens[0];
		float startPctX = Float.parseFloat(tokens[1]);
		float startPctY = Float.parseFloat(tokens[2]);
		float endPctX = Float.parseFloat(tokens[3]);
		float endPctY = Float.parseFloat(tokens[4]);
		int startAbsX = Integer.parseInt(tokens[5], 10);
		int startAbsY = Integer.parseInt(tokens[6], 10);
		int endAbsX = Integer.parseInt(tokens[7], 10);
		int endAbsY = Integer.parseInt(tokens[8], 10);

		return new AtlasSprite(name, startPctX, startPctY, endPctX, endPctY, startAbsX, startAbsY, endAbsX, endAbsY);
	}

	public AtlasSprite(String name, double startPctX, double startPctY, double endPctX, double endPctY, int startAbsX, int startAbsY, int endAbsX, int endAbsY) {
		this(name, new Position.Double2D(startPctX, startPctY, endPctX, endPctY), new Position.Integer2D(startAbsX, startAbsY, endAbsX, endAbsY));
	}

	public AtlasSprite(String name, Position.Double2D posPct, Position.Integer2D posAbs) {
		this.name = name;
		this.posPct = posPct;
		this.posAbs = posAbs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position.Double2D getPosPct() {
		return posPct;
	}

	public void setPosPct(Position.Double2D posPct) {
		this.posPct = posPct;
	}

	public Position.Integer2D getPosAbs() {
		return posAbs;
	}

	public void setPosAbs(Position.Integer2D posAbs) {
		this.posAbs = posAbs;
	}
}
