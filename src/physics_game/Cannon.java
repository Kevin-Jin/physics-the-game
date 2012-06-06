package physics_game;

public class Cannon {
	private CannonBody body;
	private CannonWheel frontWheel, rearWheel;
	private Blast blast;
	private BoundingPolygon boundPoly;

	public Cannon() {
		body = new CannonBody(this);
		frontWheel = new CannonWheel(this);
		rearWheel = new CannonWheel(this);
		blast = new Blast(this);

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

	public BoundingPolygon getBoundingPolygon() {
		return boundPoly;
	}

	public boolean flipHorizontally() {
		return body.flipHorizontally();
	}

	public Position getPosition() {
		return body.getPosition();
	}

	public void setPosition(Position pos) {
		body.setPosition(pos);
	}

	public void lookAt(Position pos) {
		blast.setRotation((float) body.lookAt(pos));
	}

	public void bodyUpdated() {
		rearWheel.sync(body.getRearWheelPosition());
		frontWheel.sync(body.getFrontWheelPosition());
		blast.sync(body.getFlamePosition());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { rearWheel.getRealBoundingPolygon(), body.getRealBoundingPolygon(), frontWheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() }));
	}
}
