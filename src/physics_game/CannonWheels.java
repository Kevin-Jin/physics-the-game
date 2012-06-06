package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CannonWheels extends BottomLeftOriginedProp {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 0), new Point2D.Double(42, 0), new Point2D.Double(42, 34), new Point2D.Double(0, 34) }) });

	private Cannon parent;

	public CannonWheels(Cannon parent) {
		super(0);
		this.parent = parent;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = baseBoundPoly;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("wheel");
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
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		
	}
}
