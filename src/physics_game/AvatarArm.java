package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class AvatarArm extends StationaryEntity {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(4, 0), new Point2D.Double(16, 12), new Point2D.Double(13, 17), new Point2D.Double(0, 4) }), new Polygon(new Point2D[] { new Point2D.Double(16, 12), new Point2D.Double(32, 12), new Point2D.Double(32, 17), new Point2D.Double(13, 17) }), new Polygon(new Point2D[] { new Point2D.Double(32, 12), new Point2D.Double(36, 8), new Point2D.Double(44, 8), new Point2D.Double(44, 20), new Point2D.Double(35, 20), new Point2D.Double(32, 17) }) });
	public static final Point2D BEAM_SOURCE = new Point2D.Double(45, 14);

	private Player parent;
	private boolean flip;

	public AvatarArm(Player parent) {
		this.parent = parent;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = baseBoundPoly;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("arm");
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return new BoundingPolygon(new Polygon[0]);
	}

	public BoundingPolygon getRealBoundingPolygon() {
		return boundPoly;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		
	}

	public void sync(Position pos, boolean flip) {
		this.pos = pos;
		this.flip = flip;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	public void setRotation(double rot) {
		this.rot = rot;
	}

	public Position getGunPosition() {
		return new Position(getTransformationMatrix().transform(BEAM_SOURCE, null));
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		
	}

	@Override
	public boolean flipHorizontally() {
		return flip;
	}

	@Override
	public Point2D getDrawPosition() {
		return new Point2D.Double(flip ? (getPosition().getX() + getOrigin().getX() * getScale().getX() - getWidth() * Math.cos(rot)) : (getPosition().getX() + getOrigin().getX() * getScale().getX()), flip ? (getPosition().getY() - getOrigin().getY() * -getScale().getY() - getWidth() * Math.sin(rot)) : getPosition().getY() - getOrigin().getY() * -getScale().getY());
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}
}
