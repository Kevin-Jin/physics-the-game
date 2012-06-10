package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class RefractionRectangle extends StationaryEntity{
	public static final double REFRACTION_INDEX = 1.5;

	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D.Double[] {
			new Point2D.Double(0, 0),
			 new Point2D.Double(11, 0),
			 new Point2D.Double(11, 159),
			 new Point2D.Double(0, 159)
	}) });
	
	private final double MIN_Y, MAX_Y;
	private final double VEL_Y = 150;
	
	public RefractionRectangle(double minY, double maxY){
		MIN_Y = minY;
		MAX_Y = maxY;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("refraction");
	}
	public void reset(){
		pos = new Position(637,0);
		vel.setY(VEL_Y);
	}
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, 0, -VEL_Y, tDelta);
		if (pos.getY() < MIN_Y){
			pos.setY(MIN_Y);
			vel.setY(VEL_Y);
		}
		if (pos.getY() > MAX_Y){
			pos.setY(MAX_Y);
			vel.setY(-VEL_Y);
		}
		
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable obj = collisionInfo.getCollidedWith();
		if (obj instanceof Wave){
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			obj.collision(collisionInfo,others);
		}
	}

}
