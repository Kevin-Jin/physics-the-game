package physics_game;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TextureCache {
	private static final Map<String, BufferedImage> loaded = new HashMap<String, BufferedImage>();

	public static BufferedImage getTexture(String key) {
		return loaded.get(key);
	}

	public static void setTexture(String key, BufferedImage value) {
		loaded.put(key, value);
	}

	public static void flush() {
		for (BufferedImage texture : loaded.values())
			texture.flush();
		loaded.clear();
	}
}
