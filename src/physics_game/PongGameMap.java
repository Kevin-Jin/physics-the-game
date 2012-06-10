package physics_game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PongGameMap extends GameMap {
	private static final NumberFormat TWO_DP = new DecimalFormat("0.00");

	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		platforms.put(Byte.valueOf((byte) 1), new Platform(-10, 1930, 620, 720));
		return new LevelLayout(1920, 1080, platforms, new Position(100,290), new Position(1168, 290), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
	}
	
	private Paddle p1, p2;
	private Wave wave;
	private RefractionRectangle refraction;
	
	public PongGameMap(String name) {
		super(name, constructLayout(), null);
		p1 = new Paddle(true);
		p2 = new Paddle(false);
		wave = new Wave();
		refraction = new RefractionRectangle(0,460);
	}

	@Override
	public Paddle getLeftPlayer() {
		return p1;
	}

	@Override
	public Paddle getRightPlayer() {
		return p2;
	}

	public void updateEntityPositions(double tDelta) {
		super.updateEntityPositions(tDelta);

		if (wave.isBetween(-10000, 0)){
			p2.addPoints(100);
			wave.reset(false);
		}
		else if (wave.isBetween(1280, 10000)){
			p1.addPoints(100);
			wave.reset(true);
		}
		else if (wave.shouldCreateFadingWave()) {
			FadingWave w = wave.getFadingWave();
			w.setEntityId(addEntity(w));
		}
	}

	@Override
	protected void resetLevel() {
		super.resetLevel();

		p1.reset();
		p1.setPosition(layout.getLeftPlayerPosition());
		addEntity(0, p1);

		p2.reset();
		p2.setPosition(layout.getRightPlayerPosition());
		addEntity(1, p2);

		wave.reset(((int) (Math.random() * 2)) == 1 ? true : false);
		addEntity(2,wave);
		
		refraction.reset();
		addEntity(3,refraction);
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		String s = "θ[i]=" + TWO_DP.format(wave.getAngleOfIncidence() * 180 / Math.PI) + "°";
		g2d.drawString(s, Game1.WIDTH / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 0 * g2d.getFontMetrics().getHeight());

		s = "θ[i]=" + TWO_DP.format(wave.getAngleOfIncidence() * 180 / Math.PI) + "°";
		g2d.drawString(s, Game1.WIDTH * 3 / 4 - g2d.getFontMetrics().stringWidth(s) / 2, Game1.HEIGHT - g2d.getFontMetrics().getDescent() - 0 * g2d.getFontMetrics().getHeight());
	}
}
