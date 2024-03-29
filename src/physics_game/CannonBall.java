package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CannonBall extends CenterOriginedProp implements Expirable {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(19, 0),
			new Point2D.Double(7, 6),
			new Point2D.Double(0, 17),
			new Point2D.Double(0, 27),
			new Point2D.Double(8, 39),
			new Point2D.Double(19, 44),
			new Point2D.Double(28, 44),
			new Point2D.Double(40, 38),
			new Point2D.Double(47, 27),
			new Point2D.Double(47, 17),
			new Point2D.Double(40, 6),
			new Point2D.Double(28, 0)
	}) });

	private static final int EXPIRE_TIME = 5;
	private static final int BASE_POP_POINTS = 100;
	private static final int COMBO_BONUS = 5;

	private boolean onGround;
	private int entityId;
	private boolean expired;
	private double remainingTime = EXPIRE_TIME;
	private boolean leftPlayer;
	private int totalPopped, pointsSinceLastFrame;

	public CannonBall(Position initPosition, double angle, double velocity, boolean leftPlayer) {
		super(-1000); //no air resistance, so this is friction
		pos = new Position(initPosition.getX() - getWidth(), initPosition.getY() - getHeight() / 2);
		vel = new Velocity(angle, velocity);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		this.leftPlayer = leftPlayer;
	}

	public boolean onGround() {
		return onGround;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		onGround = false;
		for (CollisionResult res : getCollisions(others)) {
			if (!(res.getCollisionInformation().getCollidedWith() instanceof CannonBody) && res.getCollisionInformation().getCollidingSurface().getY() == 0) {
				onGround = true;
				break;
			}
		}

		if (vel.getX() < 0 && onGround)
			vel.setX(Math.min(vel.getX() - tDelta * xDeceleration, 0));
		else if (vel.getX() > 0 && onGround)
			vel.setX(Math.max(vel.getX() + tDelta * xDeceleration, 0));
		vel.setY(Math.max(vel.getY() + yAcceleration * tDelta, yVelocityMin));

		pos.setX(pos.getX() + vel.getX() * tDelta);
		pos.setY(pos.getY() + vel.getY() * tDelta);

		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);

		if (onGround)
			remainingTime -= tDelta;
		if (pos.getX() + getWidth() < 0 || pos.getX() > Game1.WIDTH || remainingTime <= 0)
			expired = true;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("ball");
	}

	public void setEntityId(int entId) {
		entityId = entId;
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public boolean isExpired() {
		return expired;
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof Balloon){
			((Balloon) other).setExpired();
			pointsSinceLastFrame += BASE_POP_POINTS + totalPopped * COMBO_BONUS;
			totalPopped++;
			return;
		}
		super.collision(collisionInfo, others);
	}

	public boolean isLeftPlayer() {
		return leftPlayer;
	}

	public int getPointsSinceLastFrame() {
		try {
			return pointsSinceLastFrame;
		} finally {
			pointsSinceLastFrame = 0;
		}
	}
}
