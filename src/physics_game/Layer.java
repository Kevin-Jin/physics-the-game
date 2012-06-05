package physics_game;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	public static final Byte FAR_BACKGROUND = Byte.valueOf((byte) 0), MAIN_BACKGROUND = Byte.valueOf((byte) 1), MIDGROUND = Byte.valueOf((byte) 2), FOREGROUND = Byte.valueOf((byte) 3);

	private double parallax;
	private List<AbstractDrawable> drawables;

	public Layer(double parallax) {
		this.parallax = parallax;
		this.drawables = new ArrayList<AbstractDrawable>();
	}

	public double getParallaxFactor() {
		return parallax;
	}

	public List<AbstractDrawable> getDrawables() {
		return drawables;
	}
}
