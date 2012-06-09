package physics_game;

public interface Expirable {
	public int getEntityId();
	public void setEntityId(int id);
	public boolean isExpired();
}
