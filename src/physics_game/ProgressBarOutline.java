package physics_game;

import java.awt.image.BufferedImage;

public class ProgressBarOutline extends AbstractDrawable{
	private Position pos;
	
	public ProgressBarOutline(Position pos){
		this.pos=pos;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("bar");
	}

	@Override
	public double getRotation() {
		return 0;
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	@Override
	public double getWidth() {
		return getTexture().getWidth();
	}

	@Override
	public double getHeight() {
		return getTexture().getHeight();
	}

	@Override
	public Position getPosition() {
		return pos;
	}
	
}
