package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class CannonBody extends StationaryEntity {
	private double rot;
	private Cannon parent;

	public CannonBody(Cannon parent) {
		pos = new Position(100, 0);
		this.parent = parent;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		parent.bodyUpdated();
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	protected abstract Point2D getRearWheelCenter();
	protected abstract Point2D getFrontWheelCenter();
	protected abstract Point2D getSmokeCenter();

	public Position getRearWheelPosition() {
		double lastRot = rot;
		rot = 0;
		try {
			return new Position(getTransformationMatrix().transform(new Point2D.Double(getRearWheelCenter().getX() - parent.getRearWheel().getWidth() / 2, getRearWheelCenter().getY() + parent.getRearWheel().getHeight() / 2), null));
		} finally {
			rot = lastRot;
		}
	}

	public Position getFrontWheelPosition() {
		double lastRot = rot;
		rot = 0;
		try {
			return new Position(getTransformationMatrix().transform(new Point2D.Double(getFrontWheelCenter().getX() - parent.getFrontWheel().getWidth() / 2, getFrontWheelCenter().getY() + parent.getFrontWheel().getHeight() / 2), null));
		} finally {
			rot = lastRot;
		}
	}

	public Position getBlastPosition() {
		return new Position(getTransformationMatrix().transform(getSmokeCenter(), null));
	}

	public BoundingPolygon getRealBoundingPolygon() {
		return boundPoly;
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return parent.getBoundingPolygon();
	}

	@Override
	public Point2D getOrigin() {
		return new Point2D.Double(getFrontWheelCenter().getX(), getFrontWheelCenter().getY());
	}

	@Override
	public double getRotation() {
		return rot;
	}

	public void setRotation(double d) {
		rot = d;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	@Override
	public boolean transformAboutCenter() {
		return true;
	}
}
