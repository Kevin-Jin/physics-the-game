package physics_game;

import java.awt.image.BufferedImage;
import java.util.Random;

public class TestParticle extends Particle {
	private static final Random rnd = new Random();

	private int alpha;
	private int width, height;

	public TestParticle() {
		pos.setX(rnd.nextInt(Game1.WIDTH));
		pos.setY(rnd.nextInt(Game1.HEIGHT));
		acc.setX(.5);
		acc.setY(.5);
		alpha = 0xFF;
		int length = rnd.nextInt(100) + 1;
		width = length;
		height = length;
	}

	@Override
	public void recalculate(double tDelta) {
		vel.setY(vel.getY() + acc.getY() * tDelta);
		vel.setX(vel.getX() + acc.getX() * tDelta);
		pos.setX(pos.getX() + vel.getX() * tDelta);
		pos.setY(pos.getY() + vel.getY() * tDelta);
		--width;
		--height;
		--alpha;
	}

	@Override
	public boolean outOfView() {
		return alpha <= 0 || width <= 0 || height <= 0 || pos.getX() < 0 || pos.getY() >= Game1.HEIGHT || pos.getX() + width >= Game1.WIDTH || pos.getY() + height < 0;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("particletest");
	}

	@Override
	public float getAlpha() {
		return (float) alpha / 0xFF;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}
}
