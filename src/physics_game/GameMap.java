package physics_game;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class GameMap {
	private static final int FLOOR_VISIBLE_PIXELS = 50;
	private static final int CEILING_VISIBLE_PIXELS = 0;
	private static final int LEFT_WALL_VISIBLE_PIXELS = 0;
	private static final int RIGHT_WALL_VISIBLE_PIXELS = 0;

	private LevelLayout layout;
	private final Cannon leftCannon, rightCannon;
	private final SortedMap<Byte, Entity> entities;
	private final List<Particle> particles;
	private final SortedMap<Byte, Layer> layers;

	private double remainingTime;

	private CannonBall focus;

	public GameMap() {
		leftCannon = new Cannon(true);
		rightCannon = new Cannon(false);
		entities = new TreeMap<Byte, Entity>();
		particles = new ArrayList<Particle>();
		layers = new TreeMap<Byte, Layer>();

		layers.put(Layer.FAR_BACKGROUND, new Layer(0.25));
		layers.put(Layer.MAIN_BACKGROUND, new Layer(0.5));
		layers.put(Layer.MIDGROUND, new Layer(1));
		layers.put(Layer.FOREGROUND, new Layer(2));
	}

	public Cannon getLeftCannon() {
		return leftCannon;
	}

	public Cannon getRightCannon() {
		return rightCannon;
	}

	public void follow(CannonBall ball) {
		focus = ball;
	}

	public CannonBall getFocus() {
		return focus;
	}

	public SortedMap<Byte, Layer> getLayers() {
		return layers;
	}

	public SortedMap<Byte, Entity> getEntities() {
		return entities;
	}

	public Rectangle getCameraBounds() {
		return new Rectangle(-LEFT_WALL_VISIBLE_PIXELS, -FLOOR_VISIBLE_PIXELS, layout.getWidth() + LEFT_WALL_VISIBLE_PIXELS + RIGHT_WALL_VISIBLE_PIXELS, layout.getHeight() + FLOOR_VISIBLE_PIXELS + CEILING_VISIBLE_PIXELS);
	}

	public List<CollidableDrawable> getCollidables() {
		List<CollidableDrawable> list = new ArrayList<CollidableDrawable>(entities.size() + layout.getPlatforms().size());
		list.addAll(entities.values());
		list.addAll(layout.getPlatforms());
		return list;
	}

	public void setLevel(LevelLayout layout) {
		this.layout = layout;

		entities.clear();

		layers.get(Layer.MIDGROUND).getDrawables().clear();
		layers.get(Layer.MIDGROUND).getDrawables().addAll(layout.getPlatforms());

		layers.get(Layer.FAR_BACKGROUND).getDrawables().clear();
		layers.get(Layer.FAR_BACKGROUND).getDrawables().add(new DrawableTexture(layout.getOutsideBackground(), new Position(0, 0)));
		layers.get(Layer.MAIN_BACKGROUND).getDrawables().clear();
		layers.get(Layer.MAIN_BACKGROUND).getDrawables().add(new DrawableTexture(layout.getInsideBackground(), new Position(0, 0)));

		layers.get(Layer.FOREGROUND).getDrawables().clear();

		if (Double.isInfinite(layout.getExpiration())) {
			leftCannon.setPosition(layout.getLeftCannonPosition());
			addEntity((byte) 0, leftCannon.getRearWheel());
			addEntity((byte) 1, leftCannon.getBody());
			addEntity((byte) 2, leftCannon.getFrontWheel());
			addEntity((byte) 3, leftCannon.getSmoke());
			layers.get(Layer.FOREGROUND).getDrawables().add(leftCannon.getBarOutline());
			layers.get(Layer.FOREGROUND).getDrawables().add(leftCannon.getBarFill());

			rightCannon.setPosition(layout.getRightCannonPosition());
			addEntity((byte) 4, rightCannon.getRearWheel());
			addEntity((byte) 5, rightCannon.getBody());
			addEntity((byte) 6, rightCannon.getFrontWheel());
			addEntity((byte) 7, rightCannon.getSmoke());
			layers.get(Layer.FOREGROUND).getDrawables().add(rightCannon.getBarOutline());
			layers.get(Layer.FOREGROUND).getDrawables().add(rightCannon.getBarFill());
		}
		byte i = 8;
		for (BalloonSpawnInfo info : layout.getBalloons())
			addEntity(i++, new Balloon(info.getPosition(), info.getStartScale(), info.getMinimumScale(), info.getMaximumScale()));
		for (OverlayInfo info : layout.getTips())
			layers.get(Layer.FOREGROUND).getDrawables().add(new DrawableTexture(info.getWidth(), info.getHeight(), info.getImageName(), info.getPosition()));

		remainingTime = layout.getExpiration();
	}

	public void resetLevel() {
		setLevel(layout);
	}

	public void addEntity(byte entId, Entity ent) {
		entities.put(Byte.valueOf(entId), ent);
		layers.get(Layer.MIDGROUND).getDrawables().add(ent);
	}

	public byte addEntity(Entity ent) {
		byte entId = (byte) (entities.lastKey().byteValue() + 1);
		addEntity(entId, ent);
		return entId;
	}

	public void removeEntity(byte entId) {
		Entity removed = entities.remove(Byte.valueOf(entId));
		layers.get(Layer.MIDGROUND).getDrawables().remove(removed);
	}

	public void updateEntityPositions(double tDelta) {
		for (Entity ent : entities.values())
			ent.recalculate(getCollidables(), 0, layout.getGravitationalFieldStrength(), layout.getTerminalVelocity(), tDelta);
	}

	public void addParticle(Particle p) {
		particles.add(p);
		layers.get(Layer.MIDGROUND).getDrawables().add(p);
	}

	public void removeParticle(Particle p) {
		particles.remove(p);
		layers.get(Layer.MIDGROUND).getDrawables().remove(p);
	}

	public void updateParticlePositions(double tDelta) {
		for (Particle p : particles)
			p.recalculate(tDelta);
	}

	public void cleanParticles() {
		for (int i = particles.size() - 1; i >= 0; --i) {
			Particle p = particles.get(i);
			if (p.outOfView())
				removeParticle(p);
		}
	}

	public boolean isMapExpired(double tDelta) {
		if (Double.isInfinite(remainingTime))
			return false;
		remainingTime -= tDelta;
		return (remainingTime <= 0);
	}
}
