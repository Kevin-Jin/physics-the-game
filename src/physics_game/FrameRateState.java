package physics_game;

public class FrameRateState {
	private int count;
	private long start;
	private double lastFps;

	public void addFrame() {
		count++;
	}

	public double getElapsedSecondsSinceLastReset() {
		return (System.currentTimeMillis() - start) / 1000d;
	}

	public double getLastCalculatedFps() {
		return lastFps;
	}

	public void reset() {
		lastFps = count / getElapsedSecondsSinceLastReset();
		count = 0;
		start = System.currentTimeMillis();
	}
}
