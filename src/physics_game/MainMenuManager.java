package physics_game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MainMenuManager {
	public static final int SIDE_LENGTH = 3;

	private Rectangle bounds;

	private float logoScale;
	private boolean growLogo;

	private List<Button> buttons;

	public MainMenuManager(int width, int height, int numOf) {
		bounds = new Rectangle(0, 0, width, height);

		logoScale = 1;

		buttons = new ArrayList<Button>();
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void update(InputHandler controller, double tDelta) {
		if (logoScale < 0.75 && !growLogo)
			growLogo = true;
		else if (logoScale > 1.5 && growLogo)
			growLogo = false;
		if (growLogo)
			logoScale += (float) (0.3 * tDelta);
		else
			logoScale -= (float) (0.3 * tDelta);

		for (Button btn : buttons) {
			if (btn.isPointInButton(controller.getMousePosition()))
				if (controller.getCodesOfPressedMouseButtons().contains(MouseEvent.BUTTON1) && btn.isMouseDown())
					btn.act();
				else if (controller.getCodesOfPressedMouseButtons().contains(MouseEvent.BUTTON1) && !btn.isMouseDown())
					btn.setMouseDown();
				else if (!controller.getCodesOfPressedMouseButtons().contains(MouseEvent.BUTTON1) && btn.isMouseDown())
					btn.setMouseOver();
				else
					btn.setMouseOver();
			else if (btn.isMouseOver())
				btn.setMouseUp();
		}
	}

	public void draw(Graphics2D g2d) {
		AffineTransform originalTransform = g2d.getTransform();
		BufferedImage logoTexture = TextureCache.getTexture("logo");
		AffineTransform newTransform = AffineTransform.getTranslateInstance((bounds.width - logoTexture.getWidth()) / 2d + logoTexture.getWidth() / 2d, 75);
		newTransform.concatenate(AffineTransform.getScaleInstance(logoScale, logoScale));
		newTransform.concatenate(AffineTransform.getTranslateInstance(-logoTexture.getWidth() / 2d, -logoTexture.getHeight() / 2d));
		g2d.drawImage(logoTexture, newTransform, null);
		for (Button btn : buttons)
			btn.draw(g2d);
		g2d.setTransform(originalTransform);
	}
}
