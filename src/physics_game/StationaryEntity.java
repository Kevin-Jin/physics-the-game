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
		BoundingPolygon oldPoly = boundPoly;
		pos.setX(pos.getX() + deltaX);
		pos.setY(pos.getY() + deltaY);
		boundPoly = PolygonHelper.boundingPolygonRepresentingTranslation(boundPoly, new Point2D.Double(deltaX, deltaY));
		if (collidesWith(others)) {
			pos.setX(pos.getX() - deltaX);
			pos.setY(pos.getY() - deltaY);
			boundPoly = oldPoly;
		} else {
			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		}
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

		boundPoly = PolygonHelper.boundingPolygonRepresentingTranslation(boundPoly, new Point2D.Double(vel.getX() * tDelta, vel.getY() * tDelta));

		List<CollisionResult> move = getCollisions(others);
		pos.setX(pos.getX() + vel.getX() * tDelta);
		pos.setY(pos.getY() + vel.getY() * tDelta);
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		List<CollisionResult> endMove = getCollisions(others);
		Point2D minTranslation = new Point2D.Double(0, 0);
		boolean assigned = false;
		boolean flag = false;

		for (CollisionResult moveI : move) {
			CollisionInformation moveInfo = moveI.getCollisionInformation();
			CollisionInformation endOnlyInfo = null;
			CollidableDrawable moveObj = moveInfo.getCollidedWith();

			for (CollisionResult b2 : endMove) {
				if (b2.getCollisionInformation().getCollidedWith() == moveObj) {
					endOnlyInfo = b2.getCollisionInformation();
					break;
				}
			}
			if (endOnlyInfo != null) {
				if (!roundsEqual(moveInfo.getMinimumTranslationVector(), endOnlyInfo.getMinimumTranslationVector())) {
					flag = false;
					break;
				}
				flag = true;

				if (!assigned) {
					minTranslation = endOnlyInfo.getMinimumTranslationVector();
					assigned = true;
				} else {
					Point2D curTrans = endOnlyInfo.getMinimumTranslationVector();
					if (Math.signum(curTrans.getX()) == Math.signum(minTranslation.getX()) || minTranslation.getX() == 0) {
						if (Math.abs(curTrans.getX()) > Math.abs(minTranslation.getX()))
							minTranslation.setLocation(curTrans.getX(), minTranslation.getY());
					} else {
						flag = false;
						break;
					}
					if (curTrans.getY() > minTranslation.getY())
						minTranslation.setLocation(minTranslation.getX(), curTrans.getY());
				}
			} else
				flag = true;
		}

		if (move.isEmpty() || flag) {
			pos.setX(pos.getX() + minTranslation.getX());
			pos.setY(pos.getY() + minTranslation.getY());
			if (flag) {
				if (minTranslation.getY() > 0)
					vel.setY(0);
			}
		} else {
			pos.setX(pos.getX() - vel.getX() * tDelta);
			pos.setY(pos.getY() - vel.getY() * tDelta);
		}

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
		} else if (other instanceof StationaryEntity) {
			Point2D negationVector = collisionInfo.getMinimumTranslationVector();
			pos.setX(pos.getX() + negationVector.getX());
			pos.setY(pos.getY() + negationVector.getY());

			if (negationVector.getY() >= 0 && negationVector.getX() == 0) // vertical only
				vel.setY(0);
		
			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		}
	}
}
