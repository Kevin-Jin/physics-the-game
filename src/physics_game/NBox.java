package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class NBox extends CenterOriginedProp {
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(1, 133), new Point2D.Double(35, 133), new Point2D.Double(35, 1), new Point2D.Double(1, 1) }), new Polygon(new Point2D[] { new Point2D.Double(35, 35), new Point2D.Double(99, 35), new Point2D.Double(99, 1), new Point2D.Double(35, 1) }), new Polygon(new Point2D[] { new Point2D.Double(99, 133), new Point2D.Double(133, 133), new Point2D.Double(133, 1), new Point2D.Double(99, 1) }) });

	public NBox() {
		super(250);
		baseBoundPoly = BOUNDING_POLYGON;
		scale = 1.25f;
		boundPoly = BoundingPolygon.transformBoundingPolygon(BOUNDING_POLYGON, this);

	}

	public NBox(Position p) {
		super(250);
		pos.setX(p.getX());
		pos.setY(p.getY());
		baseBoundPoly = BOUNDING_POLYGON;
		scale = 1.25f;
		boundPoly = BoundingPolygon.transformBoundingPolygon(BOUNDING_POLYGON, this);
	}

	@Override
	protected double getMinimumScale() {
		return 1.25f;
	}

	@Override
	protected double getMaximumScale() {
		return 1.25f;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("n");
	}
}
