package physics_game;

import java.util.Random;

import physics_game.Balloon.BalloonColor;

public class BalloonSpawner {
	public static final Random RANDOM = new Random();

	double minTime, maxTime, time;
	public BalloonSpawner(double minTime, double maxTime){
		this.minTime = minTime;
		this.maxTime = maxTime;
	}
	public boolean update(double tdelta){
		time += tdelta;
		if(time >= maxTime || time >= minTime && RANDOM.nextInt(100) < 5){
			time = 0;
			return true;
		}
		return false;
	}
	public Balloon getRandomBalloon(){
		int x = RANDOM.nextInt(600)+ 300;
		int y = RANDOM.nextInt(300) + 50;
		return new Balloon(new Position(x,y),getRandomBalloonColor(),.5f,.5f,.5f);
	}
	public Balloon.BalloonColor getRandomBalloonColor(){
		int i = RANDOM.nextInt(4);
		switch (i){
			case 0: return BalloonColor.BLUE;
			case 1 : return BalloonColor.RED;
			case 2 : return BalloonColor.GREEN;
			default: return BalloonColor.PURPLE;
		}
	}
}
