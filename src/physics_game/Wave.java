package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Wave extends CenterOriginedProp{
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(
			new Point2D[] { 
					new Point2D.Double(0, 0), 
					new Point2D.Double(5, 0), 
					new Point2D.Double(5,19), 
					new Point2D.Double(0, 19) 
					
			})});
	
			
	private double rot;
	
	public Wave() {
		super(0);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		rot = 0;
		vel.setX(450);
		vel.setY(-450);
	}
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, 0, -1000, tDelta);
		rot =  Math.atan2(vel.getY(),vel.getX());
	}


	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("wave");
	}
	public void setPosition(Position pos){
		this.pos = pos;
	}
	public Position getPosition(){
		return pos;
	}
	@Override
	public double getWidth() {
		return super.getWidth() * 2;
	}

	@Override
	public double getHeight() {
		return super.getHeight() * 2;
	}
	public double getRotation(){
		return rot;
	}
	public boolean flipHorizontally(){
		return true;
	}

	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		Point2D d = collisionInfo.getMinimumTranslationVector();
		pos.setX(pos.getX() + d.getX());
		pos.setY(pos.getY() + d.getY());
		Point2D surface = collisionInfo.getCollidingSurface();
		boolean found = false;
		if (surface.getX() == 0){
			vel.setX(vel.getX()*-1);
			found = true;
		}
		if (surface.getY() == 0){
			vel.setY(vel.getY()*-1);
			found = true;
		}
		if (!found){
			vel.setX(vel.getX()*-1);
			vel.setY(vel.getY()*-1);
		}
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}
	

	
}
