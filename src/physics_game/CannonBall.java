package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CannonBall extends CenterOriginedProp {
	public static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { }) });

	public CannonBall(double decelX) {
		super(250);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("ball");
	}
}
