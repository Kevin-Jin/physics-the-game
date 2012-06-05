package physics_game;

public class Player {
	private AvatarBody body;
	private AvatarLeg leg;
	private AvatarArm arm;
	private JetpackFire flame;
	private BoundingPolygon boundPoly;

	public Player() {
		body = new AvatarBody(this);
		leg = new AvatarLeg(this);
		arm = new AvatarArm(this);
		flame = new JetpackFire(this);

		boundPoly = new BoundingPolygon(new BoundingPolygon[] { body.getRealBoundingPolygon(), arm.getRealBoundingPolygon(), leg.getRealBoundingPolygon(), flame.getRealBoundingPolygon() });
	}

	public AvatarBody getBody() {
		return body;
	}

	public AvatarLeg getLeg() {
		return leg;
	}

	public AvatarArm getArm() {
		return arm;
	}

	public JetpackFire getJetPackFire() {
		return flame;
	}

	public BoundingPolygon getBoundingPolygon() {
		return boundPoly;
	}

	public Position getGunPosition() {
		return arm.getGunPosition();
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

	public double getRotation() {
		return arm.getRotation();
	}

	public void lookAt(Position pos) {
		arm.setRotation(body.lookAt(pos));
		flame.setRotation(body.getRotation());
	}

	public void move(Direction dir) {
		body.move(dir);
	}

	public void bodyUpdated() {
		arm.sync(body.getArmPosition(), body.flipHorizontally());
		leg.sync(body.getLegPosition(), body.flipHorizontally());
		flame.sync(body.getFlamePosition(), body.flipHorizontally());
		boundPoly.setPolygons(BoundingPolygon.makeBoundingPolygon(new BoundingPolygon[] { body.getRealBoundingPolygon(), arm.getRealBoundingPolygon(), leg.getRealBoundingPolygon(), flame.getRealBoundingPolygon() }));
	}
}
