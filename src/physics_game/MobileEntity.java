package physics_game;

import java.awt.geom.Point2D;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class MobileEntity extends Entity {
	private final EntityPhysicalBehavior physics;
	protected final Set<Direction> moves;
	protected double remainingJump;
	protected double remainingJetpackSound;

	public MobileEntity(EntityPhysicalBehavior physics) {
		this.physics = physics;
		moves = EnumSet.noneOf(Direction.class);
		pos = new Position();
		vel = new Velocity();
		accel = new Acceleration();
	}

	public EntityPhysicalBehavior getPhysics() {
		return physics;
	}

	@Override
	public double getWidth() {
		return getTexture().getWidth();
	}

	@Override
	public double getHeight() {
		return getTexture().getHeight();
	}

	@Override
	public double getRotation() {
		return 0;
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	public void move(Direction dir) {
		moves.add(dir);
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		if (moves.contains(Direction.LEFT))
			vel.setX(Math.max(vel.getX() - tDelta * physics.getWalkAcceleration(), -physics.getMaxWalkVelocity()));
		else if (vel.getX() < 0) // friction/air resistance
			vel.setX(Math.min(vel.getX() - tDelta * physics.getStopDeceleration(), 0));
		if (moves.contains(Direction.RIGHT))
			vel.setX(Math.min(vel.getX() + tDelta * physics.getWalkAcceleration(), physics.getMaxWalkVelocity()));
		else if (vel.getX() > 0) // friction/air resistance
			vel.setX(Math.max(vel.getX() + tDelta * physics.getStopDeceleration(), 0));
		if (moves.contains(Direction.UP) && remainingJump > 0) {
			vel.setY(Math.min(vel.getY() + tDelta * physics.getJetPackAcceleration(),physics.getJetPackMaxVelocity()));
			if (vel.getY() > 0)
				remainingJump -= tDelta;
			remainingJetpackSound -= tDelta;
			if (remainingJetpackSound <= 0) {
				SoundCache.getSound("jetpack").play();
				remainingJetpackSound = SoundCache.getSound("jetpack").getDuration();
			}
		}
		// gravity
		vel.setY(Math.max(vel.getY() + yAcceleration * tDelta, yVelocityMin));

		moves.clear();

		/*boundPoly = PolygonHelper.boundingPolygonRepresentingTranslation(boundPoly, new Point2D.Double(vel.getX() * tDelta, vel.getY() * tDelta));

		if (!collidesWith(others)) {*/
			pos.setY(pos.getY() + vel.getY() * tDelta);
			pos.setX(pos.getX() + vel.getX() * tDelta);
		//}

		super.recalculate(others, xMin, yAcceleration, yVelocityMin, tDelta);
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof ExitDoor) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
		} else if (other instanceof Beam) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
		} else if (other instanceof Platform) {
			Point2D negationVector = collisionInfo.getMinimumTranslationVector();
			pos.setX(pos.getX() + negationVector.getX());
			pos.setY(pos.getY() + negationVector.getY());

			// Fall on top of horizontal line
			if (negationVector.getY() >= 0 && collisionInfo.getCollidingSurface().getY() == 0) {
				vel.setY(0);
				remainingJump = physics.getMaxJumpTime();
			} else if (negationVector.getY() < 0 && collisionInfo.getCollidingSurface().getY() == 0) {
				//pushed out of bottom
				vel.setY(0);
			}

			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		} else if (other instanceof StationaryEntity) {
			Point2D negationVector = collisionInfo.getMinimumTranslationVector();
			pos.setX(pos.getX() + negationVector.getX());
			pos.setY(pos.getY() + negationVector.getY());
			final float TOLERANCE = .0005f;
			if (negationVector.getY() >= 0 && Math.abs(negationVector.getX()) < TOLERANCE) {
				//vertical only
				vel.setY(0);
				//only reset the jump if the StationaryEntity is resting on the ground
                List<CollisionResult> collisionsWithOther = other.getCollisions(others);
				for (CollisionResult res : collisionsWithOther) {
					if (res.getCollisionInformation().getCollidedWith() != this && res.getCollisionInformation().getCollidingSurface().getY() == 0) {
						remainingJump = physics.getMaxJumpTime();
						break;
					}
				}
			}

			boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
		}
	}
}
