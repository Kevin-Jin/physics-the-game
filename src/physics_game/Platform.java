package physics_game;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Platform extends CollidableDrawable {
	private final double x1, x2, y1, y2;
	private final BoundingPolygon boundPoly;

	public Platform(double x1, double x2, double y1, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		Polygon poly = new Polygon(new Point2D[] { new Point2D.Double(x1, y1), new Point2D.Double(x2, y1), new Point2D.Double(x2, y2), new Point2D.Double(x1, y2) });
		boundPoly = new BoundingPolygon(new Polygon[] { poly });
	}

	@Override
	public BoundingPolygon getBoundingPolygon() {
		return boundPoly;
	}

	public double getLeftX() {
		return x1;
	}

	public double getRightX() {
		return x2;
	}

	public double getTopY() {
		return y1;
	}

	public double getBottomY() {
		return y2;
	}

	@Override
	public Color getTint() {
		return Color.GRAY;
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	@Override
	public Position getPosition() {
		return new Position(x1, y2);
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("platform");
	}

	@Override
	public double getWidth() {
		return Math.round(getRightX() - getLeftX());
	}

	@Override
	public double getHeight() {
		return Math.round(getTopY() - getBottomY());
	}

	@Override
	public double getRotation() {
		return 0;
	}

	@Override
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		
	}
	public String toString(){
		return "Platform at " + x1 +"," + y2;
	}
}
