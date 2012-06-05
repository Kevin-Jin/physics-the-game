package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CanonBody extends StationaryEntity {
    public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(30, 12), new Point2D.Double(18, 22), new Point2D.Double(9, 42), new Point2D.Double(9, 58), new Point2D.Double(16, 76), new Point2D.Double(27, 86), new Point2D.Double(38, 86), new Point2D.Double(50, 75), new Point2D.Double(56, 57), new Point2D.Double(56, 42), new Point2D.Double(49, 24), new Point2D.Double(35, 12) }), new Polygon(new Point2D[] { new Point2D.Double(30, 0), new Point2D.Double(30, 11), new Point2D.Double(35, 11), new Point2D.Double(35, 0) }), new Polygon(new Point2D[] { new Point2D.Double(1, 36), new Point2D.Double(9, 36), new Point2D.Double(9, 65), new Point2D.Double(1, 65) }) });
    private static final Point2D ARM_COORDINATES = new Point2D.Double(26, 55);
    private static final Point2D LEG_COORDINATES = new Point2D.Double(8, 132);
    private static final Point2D FLAME_COORDINATES = new Point2D.Double(5, 64);

	private double rot;
	private boolean flip;
	private Canon parent;

	public CanonBody(Canon parent) {
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		pos = new Position(100, 100);
		this.parent = parent;
	}

	public double lookAt(Position pos) {
		Point2D rotateAbout = getTransformationMatrix().transform(getOrigin(), null);
		double y = pos.getY() - rotateAbout.getY();
		double x = pos.getX() - rotateAbout.getX();
		double realRot = Math.atan2(y, x);
		if (flip)
			realRot -= Math.PI;
		if (!flip && x < 0 || flip && x > 0) {
			flip = !flip;
			realRot -= Math.PI;
		}

		rot = Math.min(realRot, Math.PI / 4);
		if (flip && rot < -Math.PI / 4 && rot > -Math.PI / 2)
			//uhh, kludgy check but it works!
			rot = -Math.PI / 4;
		return realRot;
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	public Position getLegPosition() {
		double lastRot = rot;
		rot = 0;
		try {
			Point2D coord = LEG_COORDINATES;
			if (flip)
				coord = new Point2D.Double(coord.getX() + 48, coord.getY());
			return new Position(getTransformationMatrix().transform(coord, null));
		} finally {
			rot = lastRot;
		}
	}

	public Position getArmPosition() {
		return new Position(getTransformationMatrix().transform(ARM_COORDINATES, null));
	}

	public Position getFlamePosition() {
		return new Position(getTransformationMatrix().transform(FLAME_COORDINATES, null));
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
		return TextureCache.getTexture("player");
	}

	@Override
	public Point2D getOrigin() {
		//TODO: figure this out
		return null;
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public boolean transformAboutCenter() {
		return true;
	}

	@Override
	public boolean flipHorizontally() {
		return flip;
	}
}
