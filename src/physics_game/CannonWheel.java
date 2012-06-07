package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CannonWheel extends BottomLeftOriginedProp {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(72, 31),
			new Point2D.Double(67, 15),
			new Point2D.Double(47, 1),
			new Point2D.Double(25, 1),
			new Point2D.Double(6, 15),
			new Point2D.Double(0, 31),
			new Point2D.Double(0, 40),
			new Point2D.Double(5, 55),
			new Point2D.Double(25, 70),
			new Point2D.Double(47, 70),
			new Point2D.Double(68, 53),
			new Point2D.Double(72, 40)
	}) });

	private Cannon parent;

	public CannonWheel(Cannon parent) {
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

	public void sync(Position pos) {
		this.pos = pos;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}
	
	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		
	}
}
