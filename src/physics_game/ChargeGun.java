package physics_game;

public class ChargeGun {
	
	private final static double MAX_ANGLE = Math.PI*2;
	private final static double MIN_ANGLE = 0;
	
	private double mult;
	private KeyBindings binding;
	private boolean positive;
	private boolean actionHeld;
	private int totalPoints;
	
	public ChargeGun(boolean p1){
		binding = new KeyBindings(p1);
		mult = (p1) ? 3 : -3;
	}
	public KeyBindings getKeyBindings() {
		return binding;
	}
	public void update(int change, boolean action, double tDelta) {
		double d = 0/*body.getRotation()*/;
		d += Math.signum(change) * tDelta * mult;
		if (d < MIN_ANGLE)
			d = MIN_ANGLE;
		if (d > MAX_ANGLE)
			d = MAX_ANGLE;
		if (action && !actionHeld)
			positive = !positive;
		actionHeld = action;
	}
	public void setPosition(Position p){
		
	}
	public void addPoints(int add) {
		totalPoints += add;
	}

	public int getPoints() {
		return totalPoints;
	}

}
