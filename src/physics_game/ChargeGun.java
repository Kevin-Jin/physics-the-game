package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class ChargeGun extends StationaryEntity implements Player {
	private static final double CHARGE = 1;

	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] {
			new Point2D.Double(60, 0),
			new Point2D.Double(11, 17),
			new Point2D.Double(1, 26),
			new Point2D.Double(1, 35),
			new Point2D.Double(11, 43),
			new Point2D.Double(28, 49),
			new Point2D.Double(57, 54),
			new Point2D.Double(103, 54),
			new Point2D.Double(135, 48),
			new Point2D.Double(154, 40),
			new Point2D.Double(159, 32),
			new Point2D.Double(159, 27),
			new Point2D.Double(149, 17),
			new Point2D.Double(94, 0)
	}) });

	private boolean flip;
	private KeyBindings binding;
	private boolean positive;
	private boolean actionHeld;
	private int totalPoints;

	public ChargeGun(boolean p1) {
		binding = new KeyBindings(p1);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
		flip = p1;
	}

	@Override
	public KeyBindings getKeyBindings() {
		return binding;
	}

	@Override
	public boolean update(int dx, int dy, boolean action, double tDelta) {
		final int MULT = 200;
			
		pos.setX(pos.getX() + dx * tDelta * MULT);
		pos.setY(pos.getY() + dy*tDelta * MULT);
		if (action && !actionHeld)
			positive = !positive;
		actionHeld = action;
		return false;
	}
	

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		//guns have no mass, therefore they are not affected by gravity
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
			Star s = (Star)other;
			if (Math.signum(s.getCharge()) != Math.signum(getCharge())) {
				s.setExpired();
				addPoints(10);
				return;
			}
		}
		if (other instanceof Asteroid){
			totalPoints -= 100;
			if (totalPoints < 0)
				totalPoints = 0;
			((Asteroid)other).setExpired();
		}
		super.collision(collisionInfo, others);
	}

	public double getCharge() {
		return positive ? CHARGE : -CHARGE;
	}

	@Override
	public void reset() {
		totalPoints = 0;
		actionHeld = false;
		positive = false;
	}
}
