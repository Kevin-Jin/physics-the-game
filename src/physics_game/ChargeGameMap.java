package physics_game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeGameMap extends GameMap {
	public static final double COULOMBS_CONSTANT = 8.99e9;

	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		platforms.put(Byte.valueOf((byte) 1), new Platform(-10, 1930, 640, 740));
		platforms.put(Byte.valueOf((byte) 2), new Platform(-100, 0, 0, 640));
		platforms.put(Byte.valueOf((byte) 3), new Platform(1280, 1380, 0, 640));
		return new LevelLayout(1920, 1080, platforms, new Position(100, 300), new Position(1140, 300), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), "", "starBG", "starBG", 90);
	}

	private final ChargeGun leftGun, rightGun;
	

	public ChargeGameMap(String name) {
		super(name, constructLayout(), getSpawners());
		leftGun = new ChargeGun(true);
		rightGun = new ChargeGun(false);
	}
	private static List<Spawner<?>> getSpawners(){
		ArrayList<Spawner<?>> spawners = new ArrayList<Spawner<?>>();
		spawners.add(new StarSpawner(.25, .6));
		spawners.add(new AsteroidSpawner(5, 15));
		return spawners;
	}

	@Override
	public ChargeGun getLeftPlayer() {
		return leftGun;
	}

	@Override
	public ChargeGun getRightPlayer() {
		return rightGun;
	}

	@Override
	protected void resetLevel() {
		super.resetLevel();

		leftGun.reset();
		leftGun.setPosition(layout.getLeftPlayerPosition());
		addEntity(0, leftGun);

		rightGun.reset();
		rightGun.setPosition(layout.getRightPlayerPosition());
		addEntity(1, rightGun);
		
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		String s = "q[blue star]=-" + Star.CHARGE + "C, q[red star]=" + Star.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 0 * g2d.getFontMetrics().getHeight());
		s = "q[blue asteroid]=-" + Asteroid.CHARGE + "C, q[red asteroid]=" + Asteroid.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 1 * g2d.getFontMetrics().getHeight());
		s = "q[source]=" + leftGun.getCharge() + "C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 2 * g2d.getFontMetrics().getHeight());
		s = "k[e]=" + COULOMBS_CONSTANT + "N*m*m/C/C";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 3 * g2d.getFontMetrics().getHeight());

		s = "q[blue star]=-" + Star.CHARGE + "C, q[red star]=" + Star.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 0 * g2d.getFontMetrics().getHeight());
		s = "q[blue asteroid]=-" + Asteroid.CHARGE + "C, q[red asteroid]=" + Asteroid.CHARGE + "C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 1 * g2d.getFontMetrics().getHeight());
		s = "q[source]=" + rightGun.getCharge() + "C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 2 * g2d.getFontMetrics().getHeight());
		s = "k[e]=" + COULOMBS_CONSTANT + "N*m*m/C/C";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 3 * g2d.getFontMetrics().getHeight());
	}
}
