package physics_game;

public class AsteroidSpawner extends Spawner<Asteroid>{

	public AsteroidSpawner(double minTime, double maxTime) {
		super(minTime, maxTime);
	}
	@Override
	public Asteroid getRandomEntity() {
		int x = RANDOM.nextInt(1280);
		int y = RANDOM.nextInt(720);
		int vx = ((RANDOM.nextBoolean()) ? 1 : -1) * 5;
		int vy = ((RANDOM.nextBoolean()) ? 1 : -1) * 100;
		Velocity v = new Velocity();
		v.setX(vx);
		v.setY(vy);
		return new Asteroid(new Position(x, y), v, RANDOM.nextBoolean());
	}
}
