package physics_game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class LightSpeedStar extends Star {
	private static Rectangle destinationRect;
	private static Point destinationCenter;
	private static double highestDistanceSquared;

	public static void setDestinationRect(Rectangle r) {
		destinationRect = r;
		destinationCenter = new Point((int) r.getCenterX(), (int) r.getCenterY());
		highestDistanceSquared = Math.pow(r.getCenterX(), 2) + Math.pow(r.getCenterY(), 2);
	}

	public LightSpeedStar(Rectangle bounds) {
		super(bounds);
	}

	public void update(double tDelta) {
		speed += .01f;
		super.update(tDelta);
	}

	private void checkBounds() {
		if (position.getX() >= destinationCenter.x - MainMenuManager.SIDE_LENGTH && position.getX() <= destinationCenter.x + MainMenuManager.SIDE_LENGTH)
			position.setLocation(position.getX() + ((rng.nextInt(2) == 0) ? -1 : 1) * (rng.nextInt(MainMenuManager.SIDE_LENGTH * 3) + MainMenuManager.SIDE_LENGTH), position.getY());

		if (position.getY() >= destinationCenter.y - MainMenuManager.SIDE_LENGTH && position.getY() <= destinationCenter.y + MainMenuManager.SIDE_LENGTH)
			position.setLocation(position.getX(), position.getY() + ((rng.nextInt(2) == 0) ? -1 : 1) * (rng.nextInt(MainMenuManager.SIDE_LENGTH * 3) + MainMenuManager.SIDE_LENGTH));
	}

	@Override
	protected void init() {
		position = new Point2D.Double(rng.nextInt(bounds.width + 1) + bounds.x, rng.nextInt(bounds.height + 1) + bounds.y);
		checkBounds();
		int starSize = (rng.nextDouble() < .2) ? 2 : 1;
		starBounds = new Rectangle((int) position.getX(), (int) position.getY(), starSize, starSize);
		isMoving = true;

		speed = Math.min(Math.max(rng.nextDouble() * 2, .5f), 2f);
		float time = (float) rng.nextDouble() * 100 + 100;
		dX = (destinationCenter.x - position.getX()) / (time * speed) * -1;
		if (Math.abs(dX) < .1)
			dX *= 5;

		dY = (destinationCenter.y - position.getY()) / (time * speed) * -1;
		if (Math.abs(dY) < .1)
			dY *= 5;

		color = getRandomColor(rng);
		twinkleColor = getRandomColor(rng);
		timeSinceLastTwinkle = MIN_TWINKLE_GAP;
	}

	@Override
	public void reset() {
		position = new Point2D.Double(rng.nextInt(bounds.width + 1) + bounds.x, rng.nextInt(bounds.height + 1) + bounds.y);
		checkBounds();
		starBounds.x = (int) position.getX();
		starBounds.y = (int) position.getY();
		speed = Math.min(Math.max(rng.nextDouble() * 2, .5f), 2f);
		double time = rng.nextDouble() * 100 + 100;

		dX = (destinationCenter.x - position.getX()) / (time * speed) * -1;
		if (Math.abs(dX) < .05)
			dX *= 5;
		if (dX == 0)
			dX = 1;

		dY = (destinationCenter.y - position.getY()) / (time * speed) * -1;
		if (Math.abs(dY) < .05)
			dY *= 3;

		timeSinceLastTwinkle = MIN_TWINKLE_GAP;
		twinkleTime = 0;
		color = getRandomColor(rng);
		twinkleColor = getRandomColor(rng);
	}
}
