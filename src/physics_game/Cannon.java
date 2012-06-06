package physics_game;

public class Cannon {
	private CannonBody body;
	private CannonWheels leg;
	private Blast blast;
	private BoundingPolygon boundPoly;

	public Cannon() {
		body = new CannonBody(this);
		leg = new CannonWheels(this);
		blast = new Blast(this);

		boundPoly = new BoundingPolygon(new BoundingPolygon[] { body.getRealBoundingPolygon(), leg.getRealBoundingPolygon(), blast.getRealBoundingPolygon() });
	}

	public CannonBody getBody() {
		return body;
	}

	public CannonWheels getLeg() {
		return leg;
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
		leg.sync(body.getLegPosition(), body.flipHorizontally());
		blast.sync(body.getFlamePosition(), body.flipHorizontally());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { body.getRealBoundingPolygon(), leg.getRealBoundingPolygon(), blast.getRealBoundingPolygon() }));
	}
}
