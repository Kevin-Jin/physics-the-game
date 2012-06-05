package physics_game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Camera {
	private final int width, height;
	private final Point2D pos;
	private Rectangle limits;

	public Camera(int width, int height) {
		this.width = width;
		this.height = height;
		pos = new Point2D.Double(0, 0);
	}

	private void checkLimits() {
		pos.setLocation(Math.min(Math.max(pos.getX(), limits.x), limits.x + limits.width - width), Math.min(Math.max(pos.getY(), height - limits.height - limits.y), -limits.y));
	}

	private void setPosition(Point2D newPos) {
		pos.setLocation(newPos);
		if (limits != null)
			checkLimits();
	}

	public void setLimits(Rectangle bounds) {
		if (bounds != null) {
			this.limits = new Rectangle(bounds.x, bounds.y, Math.max(width, bounds.width), Math.max(height, bounds.height));
			checkLimits();
		} else {
			this.limits = null;
		}
	}

	public AffineTransform getViewMatrix(double parallaxFactor) {
		AffineTransform t = AffineTransform.getTranslateInstance(-pos.getX() * parallaxFactor, -pos.getY() * parallaxFactor);
		t.concatenate(AffineTransform.getTranslateInstance(0, height));
		t.concatenate(AffineTransform.getScaleInstance(1, -1));
		return t;
	}

	public Position mouseToWorld(Point mouse) {
		AffineTransform t = AffineTransform.getTranslateInstance(0, height);
		t.concatenate(AffineTransform.getScaleInstance(1, -1));
		t.concatenate(AffineTransform.getTranslateInstance(pos.getX(), pos.getY()));
		return new Position(t.transform(mouse, null));
	}

	public void lookAt(Position newPos) {
		AffineTransform t = AffineTransform.getTranslateInstance(-width / 2d, height / 2d);
		t.concatenate(AffineTransform.getScaleInstance(1, -1));
		setPosition(t.transform(newPos.asPoint(), null));
	}
}
