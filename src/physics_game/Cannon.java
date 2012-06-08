package physics_game;

public class Cannon implements Player {
	private CannonBody body;
	private CannonWheel frontWheel, rearWheel;
	private ProgressBar bar;
	private Blast blast;
	private BoundingPolygon boundPoly;
	private KeyBindings binding;
	private double MAX_ANGLE, MIN_ANGLE;
	private int multiplier;
	private boolean actionHeld;
	private int totalPoints;

	public Cannon(boolean facingRight) {
		body = facingRight ? new LeftCannonBody(this) : new RightCannonBody(this);
		frontWheel = new CannonWheel(this);
		rearWheel = new CannonWheel(this);
		blast = new Blast(this);
		bar = new ProgressBar(new Position(0,0));
		binding = new KeyBindings(facingRight);
		boundPoly = new BoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() });
		MAX_ANGLE = (facingRight) ? 1.45 : 0; 
		MIN_ANGLE = (facingRight) ? 0: -1.34; 
		multiplier = (facingRight) ? 3 : -3;
	}

	public ProgressBarFill getBarFill() {
		return bar.getFill();
	}

	public ProgressBarOutline getBarOutline() {
		return bar.getOutline();
	}

	public ProgressBar getProgessBar() {
		return bar;
	}

	public double getPower() {
		return bar.getAmount();
	}

	public CannonBody getBody() {
		return body;
	}

	public CannonWheel getFrontWheel() {
		return frontWheel;
	}

	public CannonWheel getRearWheel() {
		return rearWheel;
	}

	public Blast getSmoke() {
		return blast;
	}

	@Override
	public KeyBindings getKeyBindings() {
		return binding;
	}

	public BoundingPolygon getBoundingPolygon() {
		return boundPoly;
	}


	public Position getPosition() {
		return body.getPosition();
	}

	public void setPosition(Position pos) {
		body.setPosition(pos);
		bar.setPosition(new Position(pos.getX() + 55, pos.getY() - 125));
	}

	@Override
	public boolean update(int change, boolean action, double tDelta) {
		boolean fired = false;
		double d = body.getRotation();
		d += Math.signum(change) * tDelta * multiplier;

		if (d < MIN_ANGLE)
			d = MIN_ANGLE;
		if (d > MAX_ANGLE)
			d = MAX_ANGLE;
		body.setRotation(d);
		blast.setRotation(d);
		bodyUpdated();
		if (actionHeld) {
			if (action)
				bar.update(tDelta);
			else
				fired = true;
		} else {
			bar.setIncreasing(action);
			bar.update(tDelta);
		}
		actionHeld = action;
		return fired;
	}

	@Override
	public void triggered(GameMap map) {
		CannonBall ball = new CannonBall(getBody().getBlastPosition(), getBody().getRotation(), getPower(), true);
		ball.setEntityId(map.addEntity(ball));
		getProgessBar().reset();
	}

	public void bodyUpdated() {
		rearWheel.sync(body.getRearWheelPosition());
		frontWheel.sync(body.getFrontWheelPosition());
		blast.sync(body.getBlastPosition());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() }));
	}

	@Override
	public void addPoints(int add) {
		totalPoints += add;
	}

	@Override
	public int getPoints() {
		return totalPoints;
	}
}
