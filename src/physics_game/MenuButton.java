package physics_game;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MenuButton extends Button {
	private static final Font FONT = new Font("Arial", Font.BOLD, 16);

	private String text;

	public MenuButton(String text, Rectangle bounds, MenuButtonHandler handler) {
		super(bounds, handler);
		this.text = text;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(FONT);
		FontMetrics textBounds = graphics.getFontMetrics();
		graphics.drawImage(TextureCache.getTexture(isMouseDown() ? "buttonPressed" : isMouseOver() ? "buttonHover" : "button"), bounds.x, bounds.y, bounds.width, bounds.height, null);
		graphics.drawString(text, (bounds.width - textBounds.stringWidth(text)) / 2 + bounds.x, (bounds.height + textBounds.getHeight()) / 2 + bounds.y);
	}
}
