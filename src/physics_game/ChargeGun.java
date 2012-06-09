package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

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
	}) });

	private final static double MAX_ANGLE = Math.PI * 2;
	private final static double MIN_ANGLE = 0;

	private boolean flip;
	private double mult, rot;
	private KeyBindings binding;
	private boolean positive;
	private boolean actionHeld;
	private int totalPoints;

	public ChargeGun(boolean p1) {
		binding = new KeyBindings(p1);
		mult = (p1) ? 3 : -3;
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		flip = p1;
	}

	@Override
	public KeyBindings getKeyBindings() {
		return binding;
	}

	@Override
	public boolean update(int change, boolean action, double tDelta) {
		rot += Math.signum(change) * tDelta * mult;
		if (rot < MIN_ANGLE)
			rot = MAX_ANGLE + rot;
		if (rot > MAX_ANGLE)
			rot = rot-MAX_ANGLE;
		if (action && !actionHeld)
			positive = !positive;
		actionHeld = action;
		return false;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		super.recalculate(others, xMin, 0, 0, tDelta);
	}

	@Override
	public void triggered(GameMap map) {

	}

	@Override
	public void setPosition(Position p) {
		pos = p;
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
	public double getRotation() {
		return rot;
	}

	@Override
	public double getWidth() {
		return super.getWidth() * 0.5;
	}

	@Override
	public double getHeight() {
		return super.getHeight() * 0.5;
	}

	@Override
	public Point2D getOrigin() {
		return new Point2D.Double((flipHorizontally() ? super.getWidth() / 2 - 153 : 153), 33);
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((positive ? "pos" : "neg") + "gun");
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	@Override
	public boolean flipHorizontally() {
		return flip;
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof Star){
			((Star) other).setExpired();
			addPoints(10);
			return;
		}
		super.collision(collisionInfo, others);
	}

	public int getCharge() {
		return positive ? 1 : -1;
	}
}
