package physics_game;

import java.util.HashMap;
import java.util.Map;

public class MapCache {
	private static final Map<String, GameMap> loaded = new HashMap<String, GameMap>();

	public static GameMap getMap(String key) {
		return loaded.get(key);
	}

	public static void initialize() {
		loaded.put("Electron Invaders", new ChargeGameMap("Electron Invaders"));
		loaded.put("Tank", new PongGameMap("Tank"));
		loaded.put("The Awesome Game", new CannonGameMap("The Awesome Game"));
		loaded.put("congratulations", new CongratulationsMap());
	}
}
