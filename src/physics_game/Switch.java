package physics_game;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Switch extends BottomLeftOriginedProp {
	private static final BoundingPolygon BASE_POLYGON = new BoundingPolygon(new Polygon[] { new Polygon(new Point2D[] { new Point2D.Double(3, 0), new Point2D.Double(34, 0), new Point2D.Double(37, 6), new Point2D.Double(23, 21), new Point2D.Double(14, 21), new Point2D.Double(0, 6) }) });

	private List<Switchable> switchables;
	private boolean isActive, wasActive;
	private Color c;
	private boolean collisionFlag;

	public Switch(Color color, Position p, List<Switchable> switchable) {
		super(0);
		pos.setX(p.getX());
		pos.setY(p.getY());
		this.switchables = switchable;
		Point2D v = p.asPoint();
		baseBoundPoly = BASE_POLYGON;
		Polygon poly = BASE_POLYGON.getPolygons()[0];
		Point2D[] vertices = new Point2D[poly.getVertexCount()];
		for (int i = 0; i < vertices.length; ++i)
			vertices[i] = new Point2D.Double(poly.getVertices()[i].getX() + v.getX(), poly.getVertices()[i].getY() + v.getY());
		Polygon newPoly = new Polygon(vertices);
		boundPoly = new BoundingPolygon(new Polygon[] { newPoly });
		c = color;
	}

	@Override
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		isActive = collisionFlag;
		collisionFlag = false;
		if (isActive && !wasActive)
			for (Switchable switchable : switchables)
				switchable.activated();

		if (!isActive && wasActive)
			for (Switchable switchable : switchables)
				switchable.deactivated();

		wasActive = isActive;
	}

	@Override
	protected double getMinimumScale() {
		return 1;
	}

	@Override
	protected double getMaximumScale() {
		return 1;
	}

	@Override
	public Color getTint() {
		return c;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("switch");
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		CollidableDrawable other = collisionInfo.getCollidedWith();
		if (other instanceof Beam) {
			collisionInfo.setCollidedWith(this);
			collisionInfo.negateMinimumTranslationVector();
			other.collision(collisionInfo, others);
		} else if (other instanceof StationaryEntity && collisionInfo.getCollidingSurface().getY() == 0) {
			collisionFlag = true;
		}
	}
}
