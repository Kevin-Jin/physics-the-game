package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ChargeGun extends StationaryEntity implements Player {
	
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
