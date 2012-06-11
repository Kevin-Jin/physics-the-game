package physics_game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CongratulationsMap extends GameMap {
	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(100,0), new Position(1000, 0), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), "", "cannonBG", "cannonBG", 90);
	}

	public CongratulationsMap() {
		super(null, constructLayout(), new ArrayList<Spawner<?>>());
	}

	@Override
	protected void resetLevel() {
		for (int i = 0; i < 1000; i++)
			addParticle(new TestParticle());
	}

	@Override
	public Player getLeftPlayer() {
		return null;
	}

	@Override
	public Player getRightPlayer() {
		return null;
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		
	}
}
