package physics_game;

import java.awt.image.BufferedImage;

public class ChargeGun extends StationaryEntity implements Player {
	private final static double MAX_ANGLE = Math.PI * 2;
	private final static double MIN_ANGLE = 0;

	private double mult;
	private KeyBindings binding;
	private boolean positive;
	private boolean actionHeld;
	private int totalPoints;

	public ChargeGun(boolean p1) {
		binding = new KeyBindings(p1);
		mult = (p1) ? 3 : -3;
	}

	@Override
	public KeyBindings getKeyBindings() {
		return binding;
	}

	@Override
	public boolean update(int change, boolean action, double tDelta) {
		double d = 0/* body.getRotation() */;
		d += Math.signum(change) * tDelta * mult;
		if (d < MIN_ANGLE)
			d = MIN_ANGLE;
		if (d > MAX_ANGLE)
			d = MAX_ANGLE;
		if (action && !actionHeld)
			positive = !positive;
		actionHeld = action;
		return false;
	}

	@Override
	public void triggered(GameMap map) {
		
	}

	public void setPosition(Position p) {

	}

	@Override
	public void addPoints(int add) {
		totalPoints += add;
	}

	@Override
	public int getPoints() {
		return totalPoints;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((positive ? "pos" : "neg") + "gun");
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}
}
