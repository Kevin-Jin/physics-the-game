package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Star extends CenterOriginedProp implements Expirable {
	public static final double CHARGE = 0.2;

	private boolean positive;
	private boolean expired;
	private int entityId;

	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(25, 0),
			new Point2D.Double(19, 18),
			new Point2D.Double(25, 26),
			new Point2D.Double(31, 18)
	}), new Polygon(new Point2D[] {
			new Point2D.Double(0, 18),
			new Point2D.Double(15, 31),
			new Point2D.Double(25, 26),
			new Point2D.Double(19, 18)
	}), new Polygon(new Point2D[] {
			new Point2D.Double(15, 31),
			new Point2D.Double(10, 49),
			new Point2D.Double(25, 38),
			new Point2D.Double(25, 26)
	}), new Polygon(new Point2D[] {
			new Point2D.Double(25, 38),
			new Point2D.Double(39, 49),
			new Point2D.Double(34, 31),
			new Point2D.Double(25, 26)
	}), new Polygon(new Point2D[] {
			new Point2D.Double(31, 18),
			new Point2D.Double(49, 18),
			new Point2D.Double(34, 31),
			new Point2D.Double(25, 26)
	}) });

	public Star(Position pos, Velocity v, boolean positive) {
		super(0);
		this.pos = pos;
		this.vel = v;
		this.positive = positive;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		Acceleration accel = new Acceleration();
		for (CollidableDrawable other : others) {
			if (other instanceof ChargeGun) {
				double xDelta = other.getPosition().getX() - getPosition().getX();
				double yDelta = other.getPosition().getY() - getPosition().getY();
				double acceleration = -ChargeGameMap.COULOMBS_CONSTANT * Game1.METERS_PER_PIXEL * getCharge() * ((ChargeGun) other).getCharge() / (xDelta * xDelta + yDelta * yDelta);
				double angle = Math.atan2(yDelta, xDelta);
				accel.setX(accel.getX() + Math.cos(angle) * acceleration);
				accel.setY(accel.getY() + Math.sin(angle) * acceleration);
			}
		}
		vel.setX(vel.getX() + accel.getX() * tDelta);
		vel.setY(vel.getY() + accel.getY() * tDelta);
		super.recalculate(others, xMin, 0, yVelocityMin, tDelta);
		if (pos.getY() >= Game1.HEIGHT+50 || pos.getY() < 0 || pos.getX() < -50 || pos.getX() >= Game1.WIDTH+50)
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
		if (other instanceof ChargeGun) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
			return;
		}
	}

	public double getCharge() {
		return positive ? CHARGE : -CHARGE;
	}
}
