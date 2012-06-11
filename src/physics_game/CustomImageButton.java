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
	private final String[] text;

	public CustomImageButton(BufferedImage image, String text, Rectangle bounds, MenuButtonHandler handler) {
		super(bounds, handler);
		this.image = image;
		this.text = text.split("\n");
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(FONT);
		FontMetrics textBounds = graphics.getFontMetrics();
		Composite originalComposite = graphics.getComposite();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, isMouseOver() ? 1 : 0.75f));
		graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
		int start = bounds.y + (bounds.height + graphics.getFontMetrics().getAscent() - graphics.getFontMetrics().getHeight() * text.length) / 2;
		for (int i = 0; i < text.length; i++)
			graphics.drawString(text[i], (bounds.width - textBounds.stringWidth(text[i])) / 2 + bounds.x, start + i * graphics.getFontMetrics().getHeight());
		graphics.setComposite(originalComposite);
	}
}
