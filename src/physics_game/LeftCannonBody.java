package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class LeftCannonBody extends CannonBody {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
		new Point2D.Double(39, 0),
		new Point2D.Double(20, 6),
		new Point2D.Double(5, 21),
		new Point2D.Double(0, 35),
		new Point2D.Double(0, 48),
		new Point2D.Double(9, 68),
		new Point2D.Double(29, 80),
		new Point2D.Double(52, 79),
		new Point2D.Double(178, 67),
		new Point2D.Double(182, 62),
		new Point2D.Double(182, 22),
		new Point2D.Double(175, 6),
		new Point2D.Double(170, 3)
	}) });
	private static final Point2D REAR_WHEEL_CENTER = new Point2D.Double(46, 86);
	private static final Point2D FRONT_WHEEL_CENTER = new Point2D.Double(37, 86);
	private static final Point2D SMOKE_CENTER = new Point2D.Double(178, 34);

	public LeftCannonBody(Cannon parent) {
		super(parent);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("body");
	}

	@Override
	protected Point2D getRearWheelCenter() {
		return REAR_WHEEL_CENTER;
	}

	@Override
	protected Point2D getFrontWheelCenter() {
		return FRONT_WHEEL_CENTER;
	}

	@Override
	protected Point2D getSmokeCenter() {
		return SMOKE_CENTER;
	}
}
