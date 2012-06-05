package physics_game;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class LevelLayout {
	private final Map<Byte, Platform> footholds;
	private final double yDeceleration;
	private final double yVelocityMin;
	private final int width;
	private final int height;
	private Position startPos, endPos;
	private List<BoxSpawnInfo> boxes;
	private List<RectangleSpawnInfo> rects;
	private List<NBoxSpawnInfo> nBoxes;
	private List<SwitchSpawnInfo> switches;
	private List<OverlayInfo> tips;
	private List<RetractablePlatform> doors;
	private String nextMap;
	private String outsideBg, insideBg;
	private double expiration;

	public LevelLayout(int width, int height, Map<Byte, Platform> footholds, Position startPos, Position endPos, int yDeceleration, int yVelocityMin, List<BoxSpawnInfo> boxes, List<RectangleSpawnInfo> rects, List<NBoxSpawnInfo> nBoxes, List<SwitchSpawnInfo> switches, List<OverlayInfo> tips, List<RetractablePlatform> doors, String nextMap, String outsideBg, String insideBg, double expiration) {
		this.width = width;
		this.height = height;
		this.footholds = footholds;
		this.startPos = startPos;
		this.endPos = endPos;
		this.yDeceleration = yDeceleration;
		this.yVelocityMin = yVelocityMin;
		this.boxes = boxes;
		this.rects = rects;
		this.nBoxes = nBoxes;
		this.switches = switches;
		this.nextMap = nextMap;
		this.tips = tips;
		this.doors = doors;
		this.outsideBg = outsideBg;
		this.insideBg = insideBg;
		this.expiration = expiration;
	}

	public double getGravitationalFieldStrength() {
		return yDeceleration;
	}

	public double getTerminalVelocity() {
		return yVelocityMin;
	}

	public Collection<Platform> getPlatforms() {
		return footholds.values();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Position getStartPosition() {
		return startPos;
	}

	public Position getEndPosition() {
		return endPos;
	}

	public List<BoxSpawnInfo> getBoxes() {
		return boxes;
	}

	public List<RectangleSpawnInfo> getRectangles() {
		return rects;
	}

	public List<NBoxSpawnInfo> getNBoxes() {
		return nBoxes;
	}

	public List<SwitchSpawnInfo> getSwitches() {
		return switches;
	}

	public List<OverlayInfo> getTips() {
		return tips;
	}

	public List<RetractablePlatform> getDoors() {
		return doors;
	}

	public String getNextMap() {
		return nextMap;
	}

	public String getOutsideBackground() {
		return outsideBg;
	}

	public String getInsideBackground() {
		return insideBg;
	}

	public double getExpiration() {
		return expiration;
	}
}
