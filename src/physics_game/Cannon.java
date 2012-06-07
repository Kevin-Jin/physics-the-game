package physics_game;

public class Cannon {
	private CannonBody body;
	private CannonWheel frontWheel, rearWheel;
	private Blast blast;
	private BoundingPolygon boundPoly;
	private KeyBindings binding;

	public Cannon() {
		body = new CannonBody(this);
		frontWheel = new CannonWheel(this);
		rearWheel = new CannonWheel(this);
		blast = new Blast(this);
		binding = new KeyBindings(true);
		boundPoly = new BoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() });
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
		final double MAX = 1.45;
		if (change == 1)
			d += DELTA;
		if (change == -1)
			d -= DELTA;
		if (d < 0)
			d = 0;
		if (d > MAX)
			d = MAX;
		
		body.setRotation(d);
		blast.setRotation(d);
		bodyUpdated();
		
	}

	public void bodyUpdated() {
		rearWheel.sync(body.getRearWheelPosition());
		frontWheel.sync(body.getFrontWheelPosition());
		blast.sync(body.getFlamePosition());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() }));
	}
}
