package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Star extends CenterOriginedProp implements Expirable {
	private boolean positive;
	private boolean expired;
	private int entityId;
	
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(22,0),
			new Point2D.Double(45,19),
			new Point2D.Double(36, 50),
			new Point2D.Double(9, 50),
			new Point2D.Double(0, 19),
	})});

	public Star(Position pos, Velocity v, boolean positive) {
		super(0);
		this.pos = pos;
		this.vel = v;
		this.positive = positive;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		/*for (CollidableDrawable other : others) {
			if (other instanceof ChargeGun) {
				double xDelta = getPosition().getX() - other.getPosition().getX();
				double yDelta = getPosition().getY() - other.getPosition().getY();
				double acceleration = 10 * getCharge() * ((ChargeGun) other).getCharge() / (xDelta * xDelta + yDelta * yDelta);
				double angle = Math.atan2(yDelta, xDelta);
				Velocity vDelta = new Velocity(acceleration * tDelta, angle);
				vel.setX(vel.getX() + vDelta.getX());
				vel.setY(vel.getY() + vDelta.getY());
			}
		}*/
		super.recalculate(others, xMin, 0, -200, tDelta);
		if (pos.getY() >= Game1.HEIGHT || pos.getY() < 0 || pos.getX() < 0 || pos.getX() >= Game1.WIDTH)
			expired = true;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("star_" + ((positive) ? "pos" : "neg"));
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public boolean isExpired() {
		return expired;
	}

	public void setExpired() {
		expired = true;
	}

	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof ChargeGun /* charge gun */) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
			return;
		}
	}

	public int getCharge() {
		return positive ? 1 : -1;
	}
}
