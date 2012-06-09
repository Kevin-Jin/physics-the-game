package physics_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CannonGameMap extends GameMap {
	private static LevelLayout constructLayout() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, -100, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(100,0), new Position(1000, 0), -400, -400, new ArrayList<OverlayInfo>(), "", "bg", "bg", 90);
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
}
