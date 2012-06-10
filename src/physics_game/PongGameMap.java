package physics_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PongGameMap extends GameMap {
	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		platforms.put(Byte.valueOf((byte) 1), new Platform(-10, 1930, 620, 720));
		platforms.put(Byte.valueOf((byte) 2), new Platform(-10, 20, 620, 0));
		platforms.put(Byte.valueOf((byte) 3), new Platform(1260, 1290, 620, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(100,290), new Position(1168, 290), -400, -400, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
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
	public Player getLeftPlayer() {
		return p1;
	}

	@Override
	public Player getRightPlayer() {
		return p2;
	}
	
	@Override
	protected void resetLevel() {
		super.resetLevel();

		p1.setPosition(layout.getLeftPlayerPosition());
		addEntity(0, p1);
		p2.setPosition(layout.getRightPlayerPosition());
		addEntity(1, p2);
		wave.setPosition(new Position(637,320));
		addEntity(2,wave);
	}

}
