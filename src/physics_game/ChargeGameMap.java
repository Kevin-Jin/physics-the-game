package physics_game;

public class ChargeGameMap extends GameMap{

	private final ChargeGun leftGun, rightGun;
	
	public ChargeGameMap(){
		super(new BalloonSpawner(.25,.6));
		leftGun = new ChargeGun(true);
		rightGun = new ChargeGun(false);
	}
	
	@Override
	public Player getLeftPlayer() {
		return leftGun;
	}

	@Override
	public Player getRightPlayer() {
		return rightGun;
	}
	@Override
	public void setLevel(LevelLayout layout) {
		super.setLevel(layout);
		
		leftGun.setPosition(layout.getLeftPlayerPosition());
		addEntity(0,leftGun);
		
		rightGun.setPosition(layout.getRightPlayerPosition());
		addEntity(1,rightGun);
	}

}
