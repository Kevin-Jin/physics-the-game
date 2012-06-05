package physics_game;

import java.awt.Color;
import java.awt.geom.Point2D;

public class RetractablePlatform extends Platform implements Switchable {
	private static final BoundingPolygon ACTIVATED = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(-1, -1), new Point2D.Double(-1, -2), new Point2D.Double(-2, -2), new Point2D.Double(-2, -1) }) });
	private boolean activated;
	private boolean defaultActivated;
	private Color c;

	public RetractablePlatform(Color c, double x1, double x2, double y1, double y2) {
		super(x1, x2, y1, y2);
		this.c = c;
		defaultActivated = false;
	}

	public RetractablePlatform(boolean defaultActivated, Color c, double x1, double x2, double y1, double y2) {
		super(x1, x2, y1, y2);
		this.c = c;
		this.defaultActivated = defaultActivated;
	}

	public void activated() {
		activated = !defaultActivated;
	}

	public void deactivated() {

		activated = defaultActivated;
	}

	public void reset() {
		activated = defaultActivated;
	}

	@Override
	public Color getTint() {
		return c;
	}

	@Override
	public double getWidth() {
		return activated ? 0 : super.getWidth();
	}

	@Override
	public double getHeight() {
		return activated ? 0 : super.getHeight();
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return (activated) ? ACTIVATED : super.getBoundingPolygon();
	}
}
