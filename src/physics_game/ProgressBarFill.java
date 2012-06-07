package physics_game;

import java.awt.image.BufferedImage;

public class ProgressBarFill extends AbstractDrawable{
	
	private Position pos;
	private ProgressBar parent;
	
	
	public ProgressBarFill(ProgressBar parent,Position pos){
		this.parent = parent;
		this.pos = pos;
	}

	@Override
	public BufferedImage getTexture() {
		parent.update();
		return TextureCache.getTexture("greenbar");
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
		return (int)parent.getAmount();
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
