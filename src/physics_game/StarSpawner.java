package physics_game;

public class StarSpawner extends Spawner<Star>{
	public StarSpawner(double minTime, double maxTime) {
		super(minTime, maxTime);
	}

	@Override
	public Star getRandomEntity() {
		int x = RANDOM.nextInt(1280);
		int y = RANDOM.nextInt(720);
		int vx = ((RANDOM.nextBoolean()) ? 1 : -1) * 10;
		int vy = ((RANDOM.nextBoolean()) ? 1 : -1) * 200;
		Velocity v = new Velocity();
		v.setX(vx);
		v.setY(vy);
		return new Star(new Position(x, y), v, RANDOM.nextBoolean());
	}
}
