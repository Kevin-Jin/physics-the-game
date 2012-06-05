package physics_game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SwitchSpawnInfo {
	private Color color;
	private Position pos;
	private List<Switchable> triggerFor;

	public SwitchSpawnInfo(Color color, Position pos, List<Switchable> switchables) {
		this.color = color;
		this.pos = pos;
		triggerFor = switchables;
	}

	public SwitchSpawnInfo(Position pos, Switchable switchable) {
		this.pos = pos;
		triggerFor = new ArrayList<Switchable>();
		triggerFor.add(switchable);
		color = Color.WHITE;
		if (switchable instanceof RetractablePlatform)
			color = ((RetractablePlatform) switchable).getTint();
	}

	public Color getColor() {
		return color;
	}

	public Position getPosition() {
		return pos;
	}

	public List<Switchable> getSwitchables() {
		return triggerFor;
	}
}
