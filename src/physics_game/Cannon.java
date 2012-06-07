package physics_game;

public class Cannon {
	private CannonBody body;
	private CannonWheel frontWheel, rearWheel;
	private Blast blast;
	private BoundingPolygon boundPoly;
	private KeyBindings binding;
	private double MAX_ANGLE, MIN_ANGLE;
	private int multiplier;

	public Cannon(boolean facingRight) {
		body = facingRight ? new LeftCannonBody(this) : new RightCannonBody(this);
		frontWheel = new CannonWheel(this);
		rearWheel = new CannonWheel(this);
		blast = new Blast(this);
		binding = new KeyBindings(facingRight);
		boundPoly = new BoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() });
		MAX_ANGLE = (facingRight) ? 1.45 : 0; 
		MIN_ANGLE = (facingRight) ? 0: -1.34; 
		multiplier = (facingRight) ? 1 : -1;
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
	public KeyBindings getKeyBindings(){
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
	}

	public void rotate(int change){
		
		double d = body.getRotation();
		final double DELTA = Math.PI/30;
		d += Math.signum(change) * DELTA*multiplier;
		
		if (d < MIN_ANGLE)
			d = MIN_ANGLE;
		if (d > MAX_ANGLE)
			d = MAX_ANGLE;
		body.setRotation(d);
		blast.setRotation(d);
		bodyUpdated();
		
	}

	public void bodyUpdated() {
		rearWheel.sync(body.getRearWheelPosition());
		frontWheel.sync(body.getFrontWheelPosition());
		blast.sync(body.getBlastPosition());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() }));
	}
}
