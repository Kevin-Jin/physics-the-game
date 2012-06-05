package physics_game;

public class RectangleSpawnInfo {
	private Position pos;
	private double initScale, minScale, maxScale;

	public RectangleSpawnInfo(Position pos, double initScale, double minScale, double maxScale) {
		this.pos = pos;
		this.initScale = initScale;
		this.minScale = minScale;
		this.maxScale = maxScale;
	}

	public Position getPosition() {
		return pos;
	}

	public double getStartScale() {
		return initScale;
	}

	public double getMinimumScale() {
		return minScale;
	}

	public double getMaximumScale() {
		return maxScale;
	}
}
