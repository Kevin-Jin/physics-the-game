package physics_game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChargeGameMap extends GameMap {
	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(100, 300), new Position(1140, 300), (int) (-9.8 / Game1.METERS_PER_PIXEL), -400, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
	}

	private final ChargeGun leftGun, rightGun;

	public ChargeGameMap() {
		super(constructLayout(), new StarSpawner(.25, .6));
		leftGun = new ChargeGun(true);
		rightGun = new ChargeGun(false);
	}

	@Override
	
	public Player getLeftPlayer() {
		return leftGun;
	}

	@Override
	public Player getRightPlayer() {
		return rightGun;
	}

	@Override
	protected void resetLevel() {
		super.resetLevel();

		leftGun.setPosition(layout.getLeftPlayerPosition());
		addEntity(0, leftGun);

		rightGun.setPosition(layout.getRightPlayerPosition());
		addEntity(1, rightGun);
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		String s = "q[blue]=-" + Star.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 0 * g2d.getFontMetrics().getHeight());
		s = "q[red]=" + Star.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 1 * g2d.getFontMetrics().getHeight());
		s = "q[source]=" + leftGun.getCharge() + "C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 2 * g2d.getFontMetrics().getHeight());
		s = "k[e]=" + Star.COULOMBS_CONSTANT + "N*m*m/C/C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 3 * g2d.getFontMetrics().getHeight());

		s = "q[blue]=-" + Star.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 0 * g2d.getFontMetrics().getHeight());
		s = "q[red]=" + Star.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 1 * g2d.getFontMetrics().getHeight());
		s = "q[source]=" + rightGun.getCharge() + "C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 2 * g2d.getFontMetrics().getHeight());
		s = "k[e]=" + Star.COULOMBS_CONSTANT + "N*m*m/C/C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 3 * g2d.getFontMetrics().getHeight());
	}
}
