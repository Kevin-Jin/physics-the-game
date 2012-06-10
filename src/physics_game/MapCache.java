package physics_game;

import java.util.HashMap;
import java.util.Map;

public class MapCache {
	private static final Map<String, GameMap> loaded = new HashMap<String, GameMap>();

	public static GameMap getMap(String key) {
		return loaded.get(key);
	}

	public static void initialize() {
		loaded.put("cannons", new CannonGameMap("cannons"));
		loaded.put("chargeguns", new ChargeGameMap("chargeguns"));
		loaded.put("pong", new PongGameMap("pong"));
	}
}
