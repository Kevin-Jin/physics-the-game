package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Asteroid extends StationaryEntity {
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D.Double[] { new Point2D.Double(89, 45),
			new Point2D.Double(54, 58),
			new Point2D.Double(28, 75),
			new Point2D.Double(15, 93),
			new Point2D.Double(9, 117),
			new Point2D.Double(10, 186),
			new Point2D.Double(125, 205),
			new Point2D.Double(191, 114),
			new Point2D.Double(98, 45)
	}), new Polygon(new Point2D.Double[] { new Point2D.Double(89, 45),
			new Point2D.Double(135, 72),
			new Point2D.Double(228, 0),
			new Point2D.Double(237, 0),
			new Point2D.Double(243, 6),
			new Point2D.Double(247, 21),
			new Point2D.Double(247, 28),
			new Point2D.Double(244, 48),
			new Point2D.Double(233, 77),
			new Point2D.Double(221, 90),
			new Point2D.Double(191, 114)
	}), new Polygon(new Point2D.Double[] { new Point2D.Double(89, 45),
			new Point2D.Double(191, 115),
			new Point2D.Double(233, 158),
			new Point2D.Double(233, 205),
			new Point2D.Double(225, 226),
			new Point2D.Double(214, 244),
			new Point2D.Double(202, 250),
			new Point2D.Double(187, 250),
			new Point2D.Double(128, 201)
	}), new Polygon(new Point2D.Double[] { new Point2D.Double(89, 45),
			new Point2D.Double(121, 204),
			new Point2D.Double(90, 239),
			new Point2D.Double(56, 248),
			new Point2D.Double(27, 248),
			new Point2D.Double(11, 244),
			new Point2D.Double(0, 233),
			new Point2D.Double(0, 221),
			new Point2D.Double(10, 186)
	}) });

	private boolean red;

	public Asteroid(boolean red) {
		this.red = red;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((red ? "r" : "b") + "asteroid");
	}

	@Override
	public boolean transformAboutCenter() {
		return true;
	}
}
