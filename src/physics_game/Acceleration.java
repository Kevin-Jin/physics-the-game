package physics_game;

public class Acceleration {
	private double x;
	private double y;

	public double getX() {
		return x;
	}

	public void setX(double value) {
		x = value;
	}

	public double getY() {
		return y;
	}

	public void setY(double value) {
		y = value;
	}

	@Override
	public String toString() {
		return "A(" + x + ", " + y + ")";
	}
}
