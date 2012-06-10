package physics_game;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CustomImageButton extends Button {
	private static final Font FONT = new Font("Arial", Font.BOLD, 16);

	private final BufferedImage image;
	private final String text;

	public CustomImageButton(BufferedImage image, String text, Rectangle bounds, MenuButtonHandler handler) {
		super(bounds, handler);
		this.image = image;
		this.text = text;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(FONT);
		FontMetrics textBounds = graphics.getFontMetrics();
		Composite originalComposite = graphics.getComposite();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, isMouseOver() ? 1 : 0.75f));
		graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
		graphics.drawString(text, (bounds.width - textBounds.stringWidth(text)) / 2 + bounds.x, (bounds.height + textBounds.getHeight()) / 2 + bounds.y);
		graphics.setComposite(originalComposite);
	}
}
