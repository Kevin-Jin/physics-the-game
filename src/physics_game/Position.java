package physics_game;

import java.awt.geom.Point2D;

public class Position {
	private double x;
	private double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Position(Point2D pt) {
		this.x = pt.getX();
		this.y = pt.getY();
	}

	public Position() {
		this(0, 0);
	}

	public Position(Position pos) {
		this(pos.x, pos.y);
	}

	public double getX() {
		return x;
	}

	public void setX(double newX) {
		this.x = newX;
	}

	public double getY() {
		return y;
	}

	public void setY(double newY) {
		this.y = newY;
	}

	public Point2D asPoint() {
		return new Point2D.Double(x, y);
	}

	@Override
	public String toString() {
		return "P(" + x +", " + y + ")";
	}
}
