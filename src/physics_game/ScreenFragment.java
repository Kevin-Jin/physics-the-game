package physics_game;

import java.awt.image.BufferedImage;

public class ScreenFragment extends AbstractDrawable {
	private final boolean centerOrigin;
	private final int width, height;
	private double rot, scale;
	private final Position pos;
	private final BufferedImage texture;
	private final Velocity vel;

	public ScreenFragment(BufferedImage texture, Position pos, double velocityAngle, double velocity) {
		centerOrigin = true;
		this.texture = texture;
		this.pos = pos;
		width = getTexture().getWidth();
		height = -getTexture().getHeight();
		scale = 1;
		vel = new Velocity(velocityAngle, velocity);
	}

	@Override
	public boolean transformAboutCenter() {
		return centerOrigin;
	}

	@Override
	public BufferedImage getTexture() {
		return texture;
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public double getWidth() {
		return scale * width;
	}

	@Override
	public double getHeight() {
		return scale * height;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	public boolean update(double rotDelta, double scaleDelta, double tDelta) {
		rot += rotDelta * tDelta;
		scale = Math.max(0, scale - scaleDelta * tDelta);
		pos.setX(pos.getX() + vel.getX() * tDelta);
		pos.setY(pos.getY() + vel.getY() * tDelta);
		return pos.getX() + getWidth() < 0 || pos.getX() > Game1.WIDTH || pos.getY() + getHeight() < 0 || pos.getY() > Game1.HEIGHT || scale == 0;
	}
}
