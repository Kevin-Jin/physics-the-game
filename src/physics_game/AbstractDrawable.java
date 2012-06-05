package physics_game;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public abstract class AbstractDrawable implements Drawable {
	public abstract boolean transformAboutCenter();

	public abstract double getWidth();

	public abstract double getHeight();

	public abstract Position getPosition();

	public boolean flipHorizontally() {
		return false;
	}

	public Color getTint() {
		return Color.WHITE;
	}

	public Point2D getOrigin() {
		return transformAboutCenter() ? new Point2D.Double(getTexture().getWidth() / 2d, getTexture().getHeight() / 2d) : new Point2D.Double(0, 0);
	}

	public Point2D getDrawPosition() {
		return new Point2D.Double(getPosition().getX() + getOrigin().getX() * getScale().getX(), getPosition().getY() - getOrigin().getY() * -getScale().getY() + getHeight());
	}

	public Point2D getScale() {
		return new Point2D.Double(getWidth() / getTexture().getWidth(), -getHeight() / getTexture().getHeight());
	}

	public AffineTransform getTransformationMatrix() {
		AffineTransform m = AffineTransform.getTranslateInstance(getDrawPosition().getX(), getDrawPosition().getY());
		m.concatenate(AffineTransform.getRotateInstance(getRotation()));
		m.concatenate(AffineTransform.getScaleInstance(getScale().getX(), getScale().getY()));
		m.concatenate(AffineTransform.getTranslateInstance(-getOrigin().getX(), -getOrigin().getY()));
		m.concatenate(AffineTransform.getTranslateInstance(getWidth() / 2, 0));
		m.concatenate(AffineTransform.getScaleInstance(flipHorizontally() ? -1 : 1, 1));
		m.concatenate(AffineTransform.getTranslateInstance(-getWidth() / 2, 0));
		return m;
	}
}
