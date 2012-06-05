package physics_game;

public abstract class BottomLeftOriginedProp extends StationaryEntity {
	public BottomLeftOriginedProp(double decelX) {
		xDeceleration = decelX;
	}

	@Override
	public boolean transformAboutCenter() {
		return false;
	}
}
