package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Asteroid extends StationaryEntity implements Expirable{
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

	public Asteroid(boolean red_as_in_positive_greg_johnson) {
		this.positive = red_as_in_positive_greg_johnson;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((positive ? "r" : "b") + "asteroid");
	}
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, 0, yVelocityMin, tDelta);
	}
	

	@Override
	public boolean transformAboutCenter() {
		return true;
	}

	@Override
	public int getEntityId() {
		return id;
	}

	@Override
	public void setEntityId(int id) {
		this.id=id;
	}
	public void setPosition(Position pos){
		this.pos = pos;
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
