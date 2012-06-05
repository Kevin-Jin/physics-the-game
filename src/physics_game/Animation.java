package physics_game;

import java.awt.image.BufferedImage;

public class Animation {
	private String[] imageNames;
	private double timeBetweenFrames;
	private double curElapsed;
	private int index;

	public Animation(String[] imageNames, double timeGapInSeconds) {
		this.imageNames = imageNames;
		timeBetweenFrames = timeGapInSeconds;
	}

	public void update(double tDelta) {
		curElapsed += tDelta;

		if (curElapsed >= timeBetweenFrames) {
			curElapsed = 0;
			index = (index + 1) % imageNames.length;
		}
	}

	public void reset() {
		curElapsed = 0;
		index = 0;
	}

	public BufferedImage getTexture() {
		return TextureCache.getTexture(imageNames[index]);
	}
}
