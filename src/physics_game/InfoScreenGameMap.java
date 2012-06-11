package physics_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InfoScreenGameMap extends GameMap{
	
	private boolean expired;
	private final static String s = "Press any key to continue...";
	
	private static LevelLayout constructLayout(String bg) {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		return new LevelLayout(1920, 1080, platforms, new Position(100,0), new Position(1000, 0), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), "", bg, bg, 30);
	}

	public InfoScreenGameMap(String name){
		super(name, constructLayout(nameToInfo(name)), new ArrayList<Spawner<?>>());
	}
	private static String nameToInfo(String s){
		if (s.equalsIgnoreCase("Electron Invaders"))
			return "chargesInfo";
		if (s.equalsIgnoreCase("Tank"))
			return "pongInfo";
		return "cannonInfo";
	}
	public GameMap getLink(){
		return MapCache.getMap(getName());
	}
	

	@Override
	public Player getLeftPlayer() {
		return null;
	}

	@Override
	public Player getRightPlayer() {
		return null;
	}
	public void respondToInput(Set<Integer> keys, double tDelta){
		if (keys.size() > 0)
			expired = true;
	}
	public boolean isMapExpired(double tDelta){
		return expired || super.isMapExpired(tDelta);
	}
	@Override
	public boolean shouldDrawPlayerDetail(){
		return false;
	}

	@Override
	public void drawSpecificDetails(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.PLAIN, 36));
		g2d.drawString(s, (Game1.WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, 300);

		
	}

}
