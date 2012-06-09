package physics_game;

import physics_game.Balloon.BalloonColor;

public class BalloonSpawner extends Spawner<Balloon> {
	public BalloonSpawner(double minTime, double maxTime) {
		super(minTime, maxTime);
	}

	@Override
	public Balloon getRandomEntity() {
		int x = RANDOM.nextInt(600) + 300;
		int y = RANDOM.nextInt(150);
		return new Balloon(new Position(x, y), getRandomBalloonColor(), .5f, .5f, .5f);
	}

	public Balloon.BalloonColor getRandomBalloonColor() {
		switch (RANDOM.nextInt(4)) {
			case 0:
				return BalloonColor.BLUE;
			case 1:
				return BalloonColor.RED;
			case 2:
				return BalloonColor.GREEN;
			default:
				return BalloonColor.PURPLE;
		}
	}
}
