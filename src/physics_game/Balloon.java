package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Balloon extends CenterOriginedProp {
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 230), new Point2D.Double(225, 230), new Point2D.Double(225, 0), new Point2D.Double(0, 0) }) });

	private double minScale;
	private double maxScale;

	public Balloon() {
		super(250);
        minScale = super.getMinimumScale();
        maxScale = super.getMaximumScale();
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	public Balloon(double startScale, double minScale, double maxScale) {
		super(250);
		scale = startScale;
		this.minScale = minScale;
		this.maxScale = maxScale;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	public Balloon(Position p, double startScale, double minScale, double maxScale) {
		super(250);
		pos = p;
		scale = startScale;
		this.minScale = minScale;
		this.maxScale = maxScale;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	protected double getMinimumScale() {
		return minScale;
	}

	@Override
	protected double getMaximumScale() {
		return maxScale;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("balloon");
	}
}
