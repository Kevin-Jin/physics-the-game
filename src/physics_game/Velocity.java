package physics_game;

public class Velocity {
	private double x;
	private double y;

	public Velocity() {
		x = y = 0;
	}

	public Velocity(double angle, double power) {
		x = Math.cos(angle) * power;
		y = Math.sin(angle) * power;
	}

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
		return "V(" + x +", " + y + ")";
	}
}
