package physics_game;

import java.awt.image.BufferedImage;

public class DrawableTexture extends AbstractDrawable {
	private final boolean centerOrigin;
	private final int width, height;
	private final double rot;
	private final Position pos;
	private final String textureName;

	public DrawableTexture(int width, int height, double rotation, Position pos, String textureName, boolean centerOrigin) {
		this.width = width;
		this.height = height;
		this.rot = rotation;
		this.pos = pos;
		this.textureName = textureName;
		this.centerOrigin = centerOrigin;
	}

	public DrawableTexture(String textureName, Position pos) {
		centerOrigin = false;
		this.textureName = textureName;
		this.pos = pos;
		width = getTexture().getWidth();
		height = getTexture().getHeight();
		rot = 0;
	}

	public DrawableTexture(int width, int height, String textureName, Position pos) {
		centerOrigin = false;
		this.width = width;
		this.height = height;
		this.pos = pos;
		this.textureName = textureName;
		rot = 0;
	}

	public DrawableTexture(double rotation, String textureName, Position pos) {
		centerOrigin = true;
		this.textureName = textureName;
		this.pos = pos;
		width = getTexture().getWidth();
		height = getTexture().getHeight();
		this.rot = rotation;
	}

	@Override
	public boolean transformAboutCenter() {
		return centerOrigin;
	}

	@Override
	public BufferedImage getTexture() {
		return TextureCache.getTexture(textureName);
	}

	@Override
	public double getRotation() {
		return rot;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public Position getPosition() {
		return pos;
	}
}
