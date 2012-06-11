package physics_game;

import java.awt.image.BufferedImage;

public class FadingWave  extends AbstractDrawable implements Expirable{
	private final double MAX_TIME = .2f;
	
	private int id;
	private double timeAlive;
	private Position pos;
	private double rot;
	
	public FadingWave(double rot, Position pos){
		this.pos = pos;
		this.rot =rot;
	}
	
	@Override
	public int getEntityId() {
		return id;
	}

	@Override
	public void setEntityId(int id) {
		this.id = id;
	}

	@Override
	public boolean isExpired() {
		return timeAlive > MAX_TIME;
	}
	public void update(double tDelta) {
		timeAlive += tDelta;
	}
	@Override
	public float getAlpha(){
		double percent = 1 - timeAlive / MAX_TIME;
		if (percent > 1)
			percent = 1;
		if (percent < 0)
			percent = 0;
		return (float)percent/2;
	}
	public Position getPosition(){
		return pos;
		
	}
	public boolean flipHorizontally() {
		return true;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture("wave");
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public boolean transformAboutCenter() {
		return true;
	}

	@Override
	public double getWidth() {
		return getTexture().getWidth()*2;
	}

	@Override
	public double getHeight() {
		return getTexture().getHeight()*2;
	}

	
}
