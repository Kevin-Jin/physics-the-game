package physics_game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CannonGameMap extends GameMap {
	private static final NumberFormat TWO_DP = new DecimalFormat("0.00");

	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(100,0), new Position(1000, 0), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
	}

	private final Cannon leftCannon, rightCannon;

	public CannonGameMap() {
		super(constructLayout(), new BalloonSpawner(.25,.6));
		leftCannon = new Cannon(true);
		rightCannon = new Cannon(false);
	}

	@Override
	public Cannon getLeftPlayer() {
		return leftCannon;
	}

	@Override
	public Cannon getRightPlayer() {
		return rightCannon;
	}

	@Override
	protected void resetLevel() {
		super.resetLevel();

		leftCannon.setPosition(layout.getLeftPlayerPosition());
		addEntity(0, leftCannon.getRearWheel());
		addEntity(1, leftCannon.getBody());
		addEntity(2, leftCannon.getFrontWheel());
		addEntity(3, leftCannon.getSmoke());
		layers.get(Layer.FOREGROUND).getDrawables().add(leftCannon.getBarOutline());
		layers.get(Layer.FOREGROUND).getDrawables().add(leftCannon.getBarFill());

		rightCannon.setPosition(layout.getRightPlayerPosition());
		addEntity(4, rightCannon.getRearWheel());
		addEntity(5, rightCannon.getBody());
		addEntity(6, rightCannon.getFrontWheel());
		addEntity(7, rightCannon.getSmoke());
		layers.get(Layer.FOREGROUND).getDrawables().add(rightCannon.getBarOutline());
		layers.get(Layer.FOREGROUND).getDrawables().add(rightCannon.getBarFill());
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		String s = "θ=" + TWO_DP.format(getLeftPlayer().getBody().getRotation() * 180 / Math.PI) + "°";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - 0 * g2d.getFontMetrics().getHeight());
		s = "v[i]=" + TWO_DP.format(getLeftPlayer().getInitialVelocity() * Game1.METERS_PER_PIXEL) + "m/s";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - 1 * g2d.getFontMetrics().getHeight());
		s = "g=" + (layout.getGravitationalFieldStrength() * Game1.METERS_PER_PIXEL) + "m/s/s";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - 2 * g2d.getFontMetrics().getHeight());

		s = "θ=" + TWO_DP.format(-getRightPlayer().getBody().getRotation() * 180 / Math.PI) + "°";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - 0 * g2d.getFontMetrics().getHeight());
		s = "v[i]=" + TWO_DP.format(getRightPlayer().getInitialVelocity() * Game1.METERS_PER_PIXEL) + "m/s";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - 1 * g2d.getFontMetrics().getHeight());
		s = "g=" + (layout.getGravitationalFieldStrength() * Game1.METERS_PER_PIXEL) + "m/s/s";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - 2 * g2d.getFontMetrics().getHeight());
	}
}
