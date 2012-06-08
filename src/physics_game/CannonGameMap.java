package physics_game;

public class CannonGameMap extends GameMap {
	private final Cannon leftCannon, rightCannon;

	public CannonGameMap() {
		leftCannon = new Cannon(true);
		rightCannon = new Cannon(false);
	}

	@Override
	public Cannon getLeftPlayer() {
		return leftCannon;
	}

	@Override
	public Cannon getRightPlayer() {
		return rightCannon;
	}

	@Override
	public void setLevel(LevelLayout layout) {
		super.setLevel(layout);

		leftCannon.setPosition(layout.getLeftCannonPosition());
		addEntity(0, leftCannon.getRearWheel());
		addEntity(1, leftCannon.getBody());
		addEntity(2, leftCannon.getFrontWheel());
		addEntity(3, leftCannon.getSmoke());
		layers.get(Layer.FOREGROUND).getDrawables().add(leftCannon.getBarOutline());
		layers.get(Layer.FOREGROUND).getDrawables().add(leftCannon.getBarFill());

		rightCannon.setPosition(layout.getRightCannonPosition());
		addEntity(4, rightCannon.getRearWheel());
		addEntity(5, rightCannon.getBody());
		addEntity(6, rightCannon.getFrontWheel());
		addEntity(7, rightCannon.getSmoke());
		layers.get(Layer.FOREGROUND).getDrawables().add(rightCannon.getBarOutline());
		layers.get(Layer.FOREGROUND).getDrawables().add(rightCannon.getBarFill());
	}
}
