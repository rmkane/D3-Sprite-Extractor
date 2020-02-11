package DiabloSpriteExtractor;

public abstract class Position<T> {
	public static class Integer2D extends Position<Integer> {
		public Integer2D(Integer startX, Integer startY, Integer endX, Integer endY) {
			super(startX, startY, endX, endY);
		}

		@Override
		public Integer getWidth() {
			return getEndX() - getStartX();
		}

		@Override
		public Integer getHeight() {
			return getEndY() - getStartY();
		}
	}
	
	public static class Float2D extends Position<Float> {
		public Float2D(Float startX, Float startY, Float endX, Float endY) {
			super(startX, startY, endX, endY);
		}

		@Override
		public Float getWidth() {
			return getEndX() - getStartX();
		}

		@Override
		public Float getHeight() {
			return getEndY() - getStartY();
		}
	}

	public static class Double2D extends Position<Double> {
		public Double2D(Double startX, Double startY, Double endX, Double endY) {
			super(startX, startY, endX, endY);
		}

		@Override
		public Double getWidth() {
			return getEndX() - getStartX();
		}

		@Override
		public Double getHeight() {
			return getEndY() - getStartY();
		}
	}

	private T startX;
	private T startY;
	private T endX;
	private T endY;

	Position(T startX, T startY, T endX, T endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	public T getStartX() {
		return startX;
	}

	public void setStartX(T startX) {
		this.startX = startX;
	}

	public T getStartY() {
		return startY;
	}

	public void setStartY(T startY) {
		this.startY = startY;
	}

	public T getEndX() {
		return endX;
	}

	public void setEndX(T endX) {
		this.endX = endX;
	}

	public T getEndY() {
		return endY;
	}

	public void setEndY(T endY) {
		this.endY = endY;
	}

	public abstract T getWidth();

	public abstract T getHeight();
}
