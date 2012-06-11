package physics_game;

public class ProgressBar {
	private ProgressBarOutline outline;
	private ProgressBarFill fill;
	private double amt;
	private double min, max;
	private boolean increasing;

	public ProgressBar(Position pos) {
		outline = new ProgressBarOutline(pos);
		fill = new ProgressBarFill(this, new Position(pos.getX() + 2, pos.getY() + 2));
		min = 0;
		max = 76;
		increasing = true;
	}

	public void update(double tDelta) {
		final double MULT = 150;
		amt += ((increasing) ? 1 : -1) * tDelta * MULT;
		if (amt < min) {
			amt = min;
			increasing = true;
		}
		if (amt > max) {
			amt = max;
			increasing = false;
		}
	}

	public void setIncreasing(boolean flag) {
		increasing = flag;
	}

	public double getAmount() {
		return amt;
	}

	public void reset() {
		amt = min;
		increasing = false;
	}

	public void setPosition(Position p) {
		outline.setPosition(p);
		fill.setPosition(new Position(p.getX() + 2, p.getY() + 2));
	}

	public ProgressBarOutline getOutline() {
		return outline;
	}

	public ProgressBarFill getFill() {
		return fill;
	}
}
