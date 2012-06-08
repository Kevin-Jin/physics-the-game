package physics_game;

import java.awt.image.BufferedImage;

public class RayGun extends StationaryEntity {
	private boolean positive;

	public RayGun(boolean positive) {
		this.positive = positive;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture((positive ? "pos" : "neg") + "raygun");
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}
}
