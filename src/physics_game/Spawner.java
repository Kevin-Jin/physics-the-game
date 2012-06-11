package physics_game;

import java.util.Random;

public abstract class Spawner<E extends Entity> {
	public static final Random RANDOM = new Random();

	private double minTime, maxTime, time;

	public Spawner(double minTime, double maxTime) {
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	public boolean update(double tdelta) {
		time += tdelta;
		if (time >= maxTime || time >= minTime && RANDOM.nextInt(100) < 5) {
			time = 0;
			return true;
		}
		return false;
	}

	public abstract E getRandomEntity();
}
