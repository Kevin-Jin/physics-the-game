package physics_game;

public interface Player {
	KeyBindings getKeyBindings();
	boolean update(int change, boolean action, double tDelta);
	void triggered(GameMap map);
	Position getPosition();
	void addPoints(int add);
	int getPoints();
	void setPosition(Position pos);
}
