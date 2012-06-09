package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ChargeGun extends StationaryEntity implements Player {
	
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 15),
			new Point2D.Double(11, 4),
			new Point2D.Double(25, 4),
			new Point2D.Double(40, 19),
			new Point2D.Double(40, 46),
			new Point2D.Double(24, 62),
			new Point2D.Double(12, 62),
			new Point2D.Double(0, 50),
	}), new Polygon(new Point2D[] {
			new Point2D.Double(40, 23),
			new Point2D.Double(57, 8),
			new Point2D.Double(106, 8),
			new Point2D.Double(149, 28),
			new Point2D.Double(155, 37),
			new Point2D.Double(155, 40),
			new Point2D.Double(149, 46),
			new Point2D.Double(113, 46),
			new Point2D.Double(105, 61),
			new Point2D.Double(57, 61),
			new Point2D.Double(48, 44),
			new Point2D.Double(40, 43),
	}), new Polygon(new Point2D[] {
			new Point2D.Double(120, 46),
			new Point2D.Double(131, 80),
			new Point2D.Double(140, 91),
			new Point2D.Double(167, 106),
			new Point2D.Double(172, 106),
			new Point2D.Double(190, 94),
			new Point2D.Double(149, 46)
	}) });
	
	
	private final static double MAX_ANGLE = Math.PI * 2;
	private final static double MIN_ANGLE = 0;

	private double mult;
	private KeyBindings binding;
	private boolean positive;
	private boolean actionHeld;
	private int totalPoints;

	public ChargeGun(boolean p1) {
		binding = new KeyBindings(p1);
		mult = (p1) ? 3 : -3;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		
	}

	@Override
	public KeyBindings getKeyBindings() {
		return binding;
	}

	@Override
	public boolean update(int change, boolean action, double tDelta) {
		double d = 0/* body.getRotation() */;
		d += Math.signum(change) * tDelta * mult;
		if (d < MIN_ANGLE)
			d = MIN_ANGLE;
		if (d > MAX_ANGLE)
			d = MAX_ANGLE;
		if (action && !actionHeld)
			positive = !positive;
		actionHeld = action;
		return false;
	}

	@Override
	public void triggered(GameMap map) {
		
	}

	public void setPosition(Position p) {

	}

	@Override
	public void addPoints(int add) {
		totalPoints += add;
	}

	@Override
	public int getPoints() {
		return totalPoints;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((positive ? "pos" : "neg") + "gun");
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}
}
