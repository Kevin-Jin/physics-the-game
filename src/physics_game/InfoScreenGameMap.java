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
	private final static String s = "Press any key to continue";
	private String dots;
	private final int y;
	private double time;
	private final double MAX_TIME = .5D;

	private static LevelLayout constructLayout(String bg, String target) {
		Map<Byte, Platform> platforms = new HashMap<Byte, Platform>();
		return new LevelLayout(1920, 1080, platforms, new Position(100,0), new Position(1000, 0), (int) (-9.8 / Game1.METERS_PER_PIXEL), Integer.MIN_VALUE, new ArrayList<OverlayInfo>(), target, bg, bg, 30);
	}

	public InfoScreenGameMap(String name){
		super(name, constructLayout(nameToInfo(name), name), 0);
		if (name.equalsIgnoreCase("Electron Invaders"))
			y = 340;
		else if (name.equalsIgnoreCase("Tank"))
			y = 325;
		else
			y = 400;
		dots = "";
	}

	private static String nameToInfo(String s){
		if (s.equalsIgnoreCase("Electron Invaders"))
			return "chargesInfo";
		if (s.equalsIgnoreCase("Tank"))
			return "pongInfo";
		return "cannonInfo";
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
	public void respondToInput(Set<Integer> keys, double tDelta){
		if (!keys.isEmpty())
			expired = true;
	}

	@Override
	public boolean isMapExpired(double tDelta){
		time += tDelta;
		if (time > MAX_TIME){
			time = 0;
			dots +='.';
			if (dots.length() >3)
				dots = "";
		}
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
		
		g2d.drawString(s+dots, (Game1.WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, y);
	}
}
