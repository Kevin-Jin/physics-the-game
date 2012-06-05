package physics_game;

import java.awt.image.BufferedImage;

public class Laserbeam extends BottomLeftOriginedProp implements Switchable {
	public Laserbeam(double x1, double x2, double y1, double y2) {
		super(0);
	}

	@Override
	public void activated() {

	}

	@Override
	public void deactivated() {

	}

	@Override
	public BufferedImage getTexture() {
		throw new UnsupportedOperationException();
	}
}
