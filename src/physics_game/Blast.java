package physics_game;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Blast extends StationaryEntity {
	private Cannon parent;
	private boolean animate, flip;
	private Animation fireAnim;

	public Blast(Cannon parent) {
		this.parent = parent;
		fireAnim = new Animation(new String[] { "blast", "blast", "blast", "blast" }, .2);
		animate = true;
		baseBoundPoly = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(0, 0), new Point2D.Double(8, 0), new Point2D.Double(8, 19), new Point2D.Double(0, 19) }) });
		boundPoly = baseBoundPoly;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public boolean flipHorizontally() {
		return flip;
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	@Override
	public BufferedImage getTexture() {
		return fireAnim.getTexture();
	}

	@Override
	public double getWidth() {
		return animate ? 8 : 0;
	}

	@Override
	public double getHeight() {
		return animate ? 19 : 0;
	}

	@Override
	public Point2D getOrigin() {
		return new Point2D.Double(getTexture().getWidth() / 2d, 0);
	}

	@Override
	public Point2D getDrawPosition() {
		return getPosition().asPoint();
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return new BoundingPolygon(new Polygon[0]);
	}

	public void setAnimate(boolean value) {
		animate = value;
	}

	public BoundingPolygon getRealBoundingPolygon() {
		return boundPoly;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		if (animate) {
			fireAnim.update(tDelta);
		}
	}

	public void sync(Position pos, boolean flip) {
		this.pos = pos;
		this.flip = flip;
		boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}

	public void setRotation(double rot) {
		this.rot = rot;
	}
}
