package physics_game;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Random;

public class Star {
	public static final float MAX_TWINKLE_TIME = .5f;
	public static final float MIN_TWINKLE_GAP = 2.0f;

	protected static final Random rng = new Random();
	protected Rectangle bounds;
	protected Rectangle starBounds;
	protected Point2D position;
	protected Color color, twinkleColor;
	protected double dY, speed, dX;

	protected float twinkleTime, timeSinceLastTwinkle;
	protected boolean isMoving, isTwinkling;

	public Star(Rectangle bounds) {
		this.bounds = bounds;
		init();
	}

	public Color getColor() {
		return (isTwinkling) ? twinkleColor : color;
	}

	public Rectangle getStarBounds() {
		return starBounds;
	}

	public static Color getRandomColor(Random rng) {
		int b = rng.nextInt(200) + 56;
		return new Color(b, b, b);
	}

	public void update(double tDelta) {
		if (isMoving) {
			double time = Math.max(Math.min(tDelta * 1000 / 4, 3), 0);
			position.setLocation(position.getX() + dX * time * speed, position.getY() + dY * time * speed);
			starBounds.x = (int) position.getX();
			starBounds.y = (int) position.getY();
			if (!isValid())
				reset();
		}
		if (isTwinkling) {
			twinkleTime += tDelta;
			if (twinkleTime > MAX_TWINKLE_TIME) {
				twinkleTime = 0;
				timeSinceLastTwinkle = 0;
				isTwinkling = false;
			}
		} else {
			timeSinceLastTwinkle += tDelta;
			if (timeSinceLastTwinkle >= MIN_TWINKLE_GAP && rng.nextDouble() < .03)
				isTwinkling = true;
		}
	}

	protected boolean isValid() {
		return position.getX() >= bounds.x && position.getX() <= bounds.x + bounds.width && position.getY() >= bounds.y && position.getY() <= bounds.y + bounds.height;
	}

	protected void init() {
		position = new Point2D.Double(rng.nextInt(bounds.width + 1) + bounds.x, rng.nextInt(bounds.height + 1) + bounds.y);
		int starSize = (rng.nextDouble() < .2) ? 2 : 1;
		starBounds = new Rectangle((int) position.getX(), (int) position.getY(), starSize, starSize);
		isMoving = rng.nextDouble() < .25;
		if (isMoving) {
			dX = rng.nextDouble() * -2;
			dY = rng.nextDouble() * -2;
			speed = rng.nextDouble() * 3;
		}
		color = getRandomColor(rng);
		twinkleColor = getRandomColor(rng);
		timeSinceLastTwinkle = MIN_TWINKLE_GAP;
	}

	public void reset() {
		position = new Point2D.Double(rng.nextInt(bounds.width + 1) + bounds.x, rng.nextInt(bounds.height + 1) + bounds.y);
		if (isMoving) {
			dX = rng.nextDouble() * -2;
			dY = rng.nextDouble() * -2;
			speed = rng.nextDouble() * 3;
		}
		timeSinceLastTwinkle = MIN_TWINKLE_GAP;
		twinkleTime = 0;
		color = getRandomColor(rng);
		twinkleColor = getRandomColor(rng);
	}
}
