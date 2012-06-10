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
	private Position leftPos, rightPos;
	private List<OverlayInfo> tips;
	private String nextMap;
	private String outsideBg, insideBg;
	private double expiration;

	public LevelLayout(int width, int height, Map<Byte, Platform> footholds, Position leftPos, Position rightPos, int yDeceleration, int yVelocityMin, List<OverlayInfo> tips, String nextMap, String outsideBg, String insideBg, double expiration) {
		this.width = width;
		this.height = height;
		this.footholds = footholds;
		this.leftPos = leftPos;
		this.rightPos = rightPos;
		this.yDeceleration = yDeceleration;
		this.yVelocityMin = yVelocityMin;
		this.nextMap = nextMap;
		this.tips = tips;
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

	public Position getLeftPlayerPosition() {
		return new Position(leftPos);
	}

	public Position getRightPlayerPosition() {
		return new Position(rightPos);
	}

	public List<OverlayInfo> getTips() {
		return tips;
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
