package physics_game;

public class TestSwitchable implements Switchable {
	@Override
	public void activated() {
		System.out.println("activated");
	}

	@Override
	public void deactivated() {
		System.out.println("deactivated");
	}
}
