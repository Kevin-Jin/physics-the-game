package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Balloon extends CenterOriginedProp implements Expirable {
	public enum BalloonColor {
		RED("r"),
		GREEN("g"),
		BLUE("b"),
		PURPLE("p");

		private final String prefix;

		private BalloonColor(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return prefix;
		}
	}

	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(44, 0),
			new Point2D.Double(28, 7),
			new Point2D.Double(17, 17),
			new Point2D.Double(7, 33),
			new Point2D.Double(0, 58),
			new Point2D.Double(0, 84),
			new Point2D.Double(5, 103),
			new Point2D.Double(14, 121),
			new Point2D.Double(28, 135),
			new Point2D.Double(44, 142),
			new Point2D.Double(60, 142),
			new Point2D.Double(73, 137),
			new Point2D.Double(87, 125),
			new Point2D.Double(97, 109),
			new Point2D.Double(104, 84),
			new Point2D.Double(104, 58),
			new Point2D.Double(99, 38),
			new Point2D.Double(88, 19),
			new Point2D.Double(71, 4),
			new Point2D.Double(60, 0)
	}) });

	private BalloonColor color;
	private double minScale;
	private double maxScale;
	private byte entityId;
	private boolean expired;

	public Balloon(BalloonColor color) {
		super(250);
		minScale = super.getMinimumScale();
		maxScale = super.getMaximumScale();
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		this.color = color;
	}

	public Balloon(double startScale, double minScale, double maxScale) {
		super(250);
		scale = startScale;
		this.minScale = minScale;
		this.maxScale = maxScale;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	public Balloon(Position p, BalloonColor c, double startScale, double minScale, double maxScale) {
		super(250);
		pos = p;
		scale = startScale;
		this.minScale = minScale;
		this.maxScale = maxScale;
		color = c;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, 0, 100, tDelta);
		if (pos.getY() >= Game1.HEIGHT)
			expired = true;
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
		return TextureCache.getTexture(color.getPrefix() + "balloon");
	}

	public void setEntityId(byte entityId) {
		this.entityId = entityId;
	}

	@Override
	public byte getEntityId() {
		return entityId;
	}

	@Override
	public boolean isExpired() {
		return expired;
	}
}
