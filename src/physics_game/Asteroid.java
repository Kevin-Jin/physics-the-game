package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Asteroid extends StationaryEntity implements Expirable{
	public static final double CHARGE = 0.01;
	
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D.Double[] { 
			new Point2D.Double(0, 34),
			new Point2D.Double(21, 5),
			new Point2D.Double(39, 0),
			new Point2D.Double(67, 0),
			new Point2D.Double(83,15),
			new Point2D.Double(95, 34),
			new Point2D.Double(95, 67),
			new Point2D.Double(71, 93),
			new Point2D.Double(55, 97),
			new Point2D.Double(37, 97),
			new Point2D.Double(21,89),
			new Point2D.Double(0, 64)
	}) });
	

	private boolean positive, expired;
	private int id;

	public Asteroid(Position p,Velocity v,boolean red_as_in_positive_greg_johnson) {
		this.positive = red_as_in_positive_greg_johnson;
		pos= p;
		vel =v;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((positive ? "r" : "b") + "asteroid");
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
		//stars have no mass, therefore they are not affected by gravity
		super.recalculate(others, xMin, 0, yVelocityMin, tDelta);
		if (pos.getY() >= Game1.HEIGHT+50 || pos.getY() < 0 || pos.getX() < -50 || pos.getX() >= Game1.WIDTH+50)
			expired = true;
	}
	
	

	@Override
	public boolean transformAboutCenter() {
		return true;
	}

	@Override
	public int getEntityId() {
		return id;
	}
	public double getCharge(){
		return (positive) ? CHARGE : -CHARGE;
	}

	@Override
	public void setEntityId(int id) {
		this.id=id;
	}
	public double getWidth(){
		return super.getWidth() *.5;
	}
	public double getHeight(){
		return super.getHeight() *.5;
	}

	@Override
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(){
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
}
