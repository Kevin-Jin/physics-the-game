package physics_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelCache {
	private static final Map<String, LevelLayout> loaded = new HashMap<String, LevelLayout>();

	public static LevelLayout getLevel(String key) {
		return loaded.get(key);
	}

	public static void setLevel(String key, LevelLayout value) {
		loaded.put(key, value);
	}

	private static LevelLayout constructLevel1() {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		platforms.put(Byte.valueOf((byte) 0), new Platform(-10, 1930, 0, 0));
		return new LevelLayout(1920, 1080, platforms, new Position(0, 0), -400, -400, new ArrayList<BalloonSpawnInfo>(), new ArrayList<OverlayInfo>(), "", "logo", "logo", Double.POSITIVE_INFINITY);
	}

	public static void initialize() {
		loaded.put("level1", constructLevel1());
	}
}
