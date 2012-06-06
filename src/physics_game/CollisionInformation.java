package physics_game;

import java.awt.geom.Point2D;

public class CollisionInformation {
	private Point2D minTranslation, surface;
	private CollidableDrawable otherDrawable;

	public CollisionInformation(Point2D toNegate, Point2D surface, CollidableDrawable collidedWith) {
		this.minTranslation = toNegate;
		this.surface = surface;
	}

	public void negateMinimumTranslationVector() {
		minTranslation.setLocation(minTranslation.getX() * -1, minTranslation.getY() * -1);
	}

	public Point2D getMinimumTranslationVector() {
		return minTranslation;
	}

	public Point2D getCollidingSurface() {
		return surface;
	}

	public CollidableDrawable getCollidedWith() {
		return otherDrawable;
	}

	public void setCollidedWith(CollidableDrawable value) {
		otherDrawable = value;
	}

	@Override
	public String toString() {
		return "Translation vector of " + minTranslation + " and surface vector of : " + surface;
	}
}
