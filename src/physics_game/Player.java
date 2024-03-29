package physics_game;

public interface Player {
	KeyBindings getKeyBindings();
	boolean update(int dx, int dy, boolean action, double tDelta);
	void triggered(GameMap map);
	Position getPosition();
	void addPoints(int add);
	int getPoints();
	void setPosition(Position pos);
	void reset();
}
