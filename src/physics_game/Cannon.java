package physics_game;

public class Cannon {
	private CannonBody body;
	private CannonWheel wheel;
	private Blast blast;
	private BoundingPolygon boundPoly;

	public Cannon() {
		body = new CannonBody(this);
		wheel = new CannonWheel(this);
		blast = new Blast(this);

		boundPoly = new BoundingPolygon(new BoundingPolygon[] { body.getRealBoundingPolygon(), wheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() });
	}

	public CannonBody getBody() {
		return body;
	}

	public CannonWheel getLeg() {
		return wheel;
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
		wheel.sync(body.getFrontWheelPosition(), body.flipHorizontally());
		blast.sync(body.getFlamePosition(), body.flipHorizontally());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { body.getRealBoundingPolygon(), wheel.getRealBoundingPolygon(), blast.getRealBoundingPolygon() }));
	}
}
