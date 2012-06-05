package physics_game;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class MenuButton {
	public interface MenuButtonHandler {
		public void clicked();
	}

	private String text;
	private Rectangle bounds;
	private boolean inactive, over, down;
	private MenuButtonHandler handler;

	public MenuButton(String text, Rectangle bounds, MenuButtonHandler handler) {
		this.text = text;
		this.bounds = bounds;
		this.handler = handler;
	}

	public void act() {
		if (!inactive) {
			handler.clicked();
			inactive = true;
		}
	}

	public void draw(Graphics2D graphics) {
		FontMetrics textBounds = graphics.getFontMetrics();
		graphics.drawImage(TextureCache.getTexture(down ? "buttonPressed" : over ? "buttonHover" : "button"), bounds.x, bounds.y, bounds.width, bounds.height, null);
		graphics.drawString(text, (bounds.width - textBounds.stringWidth(text)) / 2 + bounds.x, (bounds.height - textBounds.getHeight()) / 2 + bounds.y);
	}

	public boolean isPointInButton(Point point) {
		return point.x > bounds.x && point.x < bounds.x + bounds.width && point.y > bounds.y && point.y < bounds.y + bounds.height;
	}

	public void setMouseDown() {
		over = down = true;
	}

	public void setMouseUp() {
		over = down = false;
		inactive = false;
	}

	public void setMouseOver() {
		over = true;
		down = false;
		inactive = false;
	}

	public boolean isMouseUp() {
		return !over && !down;
	}

	public boolean isMouseOver() {
		return over;
	}

	public boolean isMouseDown() {
		return down;
	}
}
