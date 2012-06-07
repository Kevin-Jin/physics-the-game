package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CannonBall extends CenterOriginedProp {
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

	private boolean onGround;

	public CannonBall(Position initPosition, double angle, double power) {
		super(-1000);
		pos = new Position(initPosition.getX() - getWidth(), initPosition.getY() - getHeight() / 2);
		vel = new Velocity(angle, power * 10);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
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
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("ball");
	}
}
