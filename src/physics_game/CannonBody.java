package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CannonBody extends StationaryEntity {
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
	private static final Point2D FRONT_WHEEL_CENTER = new Point2D.Double(37, 86);
	private static final Point2D SMOKE_CENTER = new Point2D.Double(178, 34);

	private double rot;
	private Cannon parent;

	public CannonBody(Cannon parent) {
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		pos = new Position(100, 100);
		this.parent = parent;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, yAcceleration, yVelocityMin, tDelta);
		parent.bodyUpdated();
	}

	public double lookAt(Position pos) {
		Point2D rotateAbout = getTransformationMatrix().transform(getOrigin(), null);
		double y = pos.getY() - rotateAbout.getY();
		double x = pos.getX() - rotateAbout.getX();
		double realRot = Math.atan2(y, x);
		if (x < 0)
			realRot -= Math.PI;

		rot = realRot;
		return realRot;
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	public Position getFrontWheelPosition() {
		double lastRot = rot;
		rot = 0;
		try {
			return new Position(getTransformationMatrix().transform(new Point2D.Double(FRONT_WHEEL_CENTER.getX() - parent.getLeg().getWidth() / 2, FRONT_WHEEL_CENTER.getY() + parent.getLeg().getHeight() / 2), null));
		} finally {
			rot = lastRot;
		}
	}

	public Position getFlamePosition() {
		return new Position(getTransformationMatrix().transform(SMOKE_CENTER, null));
	}

	public BoundingPolygon getRealBoundingPolygon() {
		return boundPoly;
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return parent.getBoundingPolygon();
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("body");
	}

	@Override
	public Point2D getOrigin() {
		return new Point2D.Double(FRONT_WHEEL_CENTER.getX(), FRONT_WHEEL_CENTER.getY());
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public boolean transformAboutCenter() {
		return true;
	}
}
