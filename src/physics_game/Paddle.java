package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Paddle extends StationaryEntity implements Player {
	private static final BoundingPolygon BOUNDING_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D.Double[] {
			new Point2D.Double(0, 0),
			 new Point2D.Double(11, 0),
			 new Point2D.Double(11, 79),
			 new Point2D.Double(0, 79)
	}) });
	
	private KeyBindings binding;
	private int points;

	public Paddle(boolean left) {
		binding = new KeyBindings(left);
		baseBoundPoly = BOUNDING_POLYGON;
		boundPoly = BOUNDING_POLYGON;
	}

	@Override
	public KeyBindings getKeyBindings() {
		return binding;
	}

	@Override
	public boolean update(int dx, int dy, boolean action, double tDelta) {
		final int MULT = 900;
		double y = pos.getY() + dy * tDelta * MULT;
		if (y < 0)
			y = 0;
		if (y > 540)
			y = 540;
		pos.setY(y);
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
	public void addPoints(int add) {
		points += add;
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public void setPosition(Position pos) {
		this.pos = pos;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("paddle");
	}

	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof Wave) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
			return;
		}
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	@Override
	public void reset() {
		points = 0;
	}
}
