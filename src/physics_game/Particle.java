package physics_game;

public abstract class Particle extends AbstractDrawable {
	protected Position pos;
	protected Velocity vel;
	protected Acceleration acc;

	public Particle() {
		this(new Position(), new Velocity(), new Acceleration());
	}

	public Particle(Position p, Velocity v, Acceleration a) {
		pos = p;
		vel = v;
		acc = a;
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}

	@Override
	public double getRotation() {
		return 0;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	public abstract boolean outOfView();

	public abstract void recalculate(double tDelta);
}
