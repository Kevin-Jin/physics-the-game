package physics_game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Button {
	public interface MenuButtonHandler {
		public void clicked();
	}

	protected final Rectangle bounds;
	private final MenuButtonHandler handler;
	private boolean inactive, over, down;

	protected Button(Rectangle bounds, MenuButtonHandler handler) {
		this.bounds = bounds;
		this.handler = handler;
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

	public void act() {
		if (!inactive) {
			handler.clicked();
			inactive = true;
		}
	}

	public abstract void draw(Graphics2D graphics);
}
