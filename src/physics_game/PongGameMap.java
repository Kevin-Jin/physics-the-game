package physics_game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PongGameMap extends GameMap {
	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		platforms.put(Byte.valueOf((byte) 1), new Platform(-10, 1930, 620, 720));
		return new LevelLayout(1920, 1080, platforms, new Position(100,290), new Position(1168, 290), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
	}
	
	private Paddle p1, p2;
	private Wave wave;
	
	public PongGameMap() {
		super(constructLayout(), null);
		p1 = new Paddle(true);
		p2 = new Paddle(false);
		wave = new Wave();
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
			wave.reset(new Position(637,320));
		}
		if (wave.isBetween(1280, 10000)){
			p1.addPoints(100);
			wave.reset(new Position(637,320));
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

		wave.reset(new Position(637,320));
		addEntity(2,wave);
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		
	}
}
