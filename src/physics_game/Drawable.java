package physics_game;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public interface Drawable {
	Point2D getOrigin();
	Point2D getDrawPosition();
	BufferedImage getTexture();
	double getRotation();
	Point2D getScale();
	boolean flipHorizontally();
	Color getTint();
}
