package physics_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChargeGameMap extends GameMap {
	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(100, 300), new Position(1140, 300), -400, -400, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
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
}
