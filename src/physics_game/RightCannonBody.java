package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class RightCannonBody extends CannonBody {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(185 - 39, 0),
			new Point2D.Double(185 - 20, 6),
			new Point2D.Double(185 - 5, 21),
			new Point2D.Double(185 - 0, 35),
			new Point2D.Double(185 - 0, 48),
			new Point2D.Double(185 - 9, 68),
			new Point2D.Double(185 - 29, 80),
			new Point2D.Double(185 - 52, 79),
			new Point2D.Double(185 - 178, 67),
			new Point2D.Double(185 - 182, 62),
			new Point2D.Double(185 - 182, 22),
			new Point2D.Double(185 - 175, 6),
			new Point2D.Double(185 - 170, 3)
	}) });
	private static final Point2D REAR_WHEEL_CENTER = new Point2D.Double(180 - 46, 86);
	private static final Point2D FRONT_WHEEL_CENTER = new Point2D.Double(180 - 37, 86);
	private static final Point2D SMOKE_CENTER = new Point2D.Double(180 - 178, 34);

	public RightCannonBody(Cannon parent) {
		super(parent);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("rbody");
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
