package physics_game;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class StationaryEntity extends Entity {
	protected double scale;
	protected double xDeceleration;
	protected double rot;
	private boolean continueRotating;
	private double startRot;

	public StationaryEntity() {
		scale = 1;
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public double getWidth() {
		return scale * getTexture().getWidth();
	}

	@Override
	public double getHeight() {
		return scale * getTexture().getHeight();
	}

	protected double getMinimumScale() {
		return 0.1;
	}

	protected double getMaximumScale() {
		return 10;
	}

	protected double getShrinkRate() {
		return 1;
	}

	protected double getStretchRate() {
		return 1;
	}

	protected double getRotateAngularVelocity() {
		return Math.PI;
	}

	public void stretch(List<CollidableDrawable> others, double tDelta) {
		double oldScale = scale;
		BoundingPolygon oldPoly = boundPoly;
		scale = Math.min(Math.max(scale + getStretchRate() * tDelta, getMinimumScale()), getMaximumScale());
		if (scale != oldScale) {
			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
			if (collidesWith(others)) {
				scale = oldScale;
				boundPoly = oldPoly;
			}
		}
	}

	public void shrink(double tDelta) {
		scale = Math.min(Math.max(scale - getShrinkRate() * tDelta, getMinimumScale()), getMaximumScale());
	}

	public void rotate(List<CollidableDrawable> others, double tDelta) {
		continueRotating = true;
		startRot = rot;
	}

	public void drag(List<CollidableDrawable> others, double deltaX, double deltaY) {
		pos.setX(pos.getX() + deltaX);
		pos.setY(pos.getY() + deltaY);
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		
	}

	protected boolean roundsEqual(Point2D v1, Point2D v2) {
		final float TOLERANCE = .0005f;
		return Math.abs(v1.getX() - v2.getX()) < TOLERANCE && Math.abs(v1.getY() - v2.getY()) < TOLERANCE;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		if (continueRotating)
			rot += getRotateAngularVelocity() * tDelta;
		else if (rot / (Math.PI / 2) != (int) (rot / (Math.PI / 2)))
			rot = Math.min(rot + getRotateAngularVelocity() * tDelta, (Math.floor(startRot / (Math.PI / 2)) + 1) * Math.PI / 2);
		continueRotating = false;

		if (vel.getX() < 0)
			vel.setX(Math.min(vel.getX() - tDelta * xDeceleration, 0));
		else if (vel.getX() > 0)
			vel.setX(Math.max(vel.getX() + tDelta * xDeceleration, 0));
		vel.setY(Math.max(vel.getY() + yAcceleration * tDelta, yVelocityMin));

		
		pos.setX(pos.getX() + vel.getX() * tDelta);
		pos.setY(pos.getY() + vel.getY() * tDelta);
		//boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		
		super.recalculate(others, xMin, yAcceleration, yVelocityMin, tDelta);
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof Platform) {
			
			Point2D negationVector = collisionInfo.getMinimumTranslationVector();
			
			pos.setX(pos.getX() + negationVector.getX());
			pos.setY(pos.getY() + negationVector.getY());

			// Fall on top of horizontal line or pushed out of button
			if (negationVector.getY() >= 0 && collisionInfo.getCollidingSurface().getY() == 0
					|| negationVector.getY() < 0 && collisionInfo.getCollidingSurface().getY() == 0)
				vel.setY(0);

			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		} 
		/*
		else if (other instanceof StationaryEntity) {
			Point2D negationVector = collisionInfo.getMinimumTranslationVector();
			pos.setX(pos.getX() + negationVector.getX());
			pos.setY(pos.getY() + negationVector.getY());

			if (negationVector.getY() >= 0 && negationVector.getX() == 0) // vertical only
				vel.setY(0);
		
			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		}*/
	}
}
