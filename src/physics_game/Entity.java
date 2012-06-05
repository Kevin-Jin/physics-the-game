package physics_game;

import java.util.List;

public abstract class Entity extends CollidableDrawable {
	protected Position pos;
	protected Velocity vel;
	protected Acceleration accel;
	protected BoundingPolygon baseBoundPoly;
	protected BoundingPolygon boundPoly;

	public Entity() {
		pos = new Position();
		vel = new Velocity();
		accel = new Acceleration();
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	public Velocity getVelocity() {
		return vel;
	}

	public Acceleration getAcceleration() {
		return accel;
	}

	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return boundPoly;
	}
}
