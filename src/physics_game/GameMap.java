package physics_game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class GameMap {
	private static final int FLOOR_VISIBLE_PIXELS = 80;
	private static final int CEILING_VISIBLE_PIXELS = 0;
	private static final int LEFT_WALL_VISIBLE_PIXELS = 0;
	private static final int RIGHT_WALL_VISIBLE_PIXELS = 0;

	private final String name;
	protected final LevelLayout layout;
	private final SortedMap<Integer, Entity> entities;
	private final List<Particle> particles;
	protected final SortedMap<Byte, Layer> layers;

	protected Spawner<?> spawner;

	private double remainingTime;

	public GameMap(String name, LevelLayout layout, Spawner<?> spawner) {
		this.name = name;
		this.layout = layout;
		this.spawner = spawner;
		entities = new TreeMap<Integer, Entity>();
		particles = new ArrayList<Particle>();
		layers = new TreeMap<Byte, Layer>();
		layers.put(Layer.FAR_BACKGROUND, new Layer(0.25));
		layers.put(Layer.MAIN_BACKGROUND, new Layer(0.5));
		layers.put(Layer.MIDGROUND, new Layer(1));
		layers.put(Layer.FOREGROUND, new Layer(2));
	}

	public abstract Player getLeftPlayer();
	public abstract Player getRightPlayer();

	public SortedMap<Byte, Layer> getLayers() {
		return layers;
	}

	public SortedMap<Integer, Entity> getEntities() {
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

	protected void resetLevel() {
		entities.clear();

		layers.get(Layer.MIDGROUND).getDrawables().clear();
		layers.get(Layer.MIDGROUND).getDrawables().addAll(layout.getPlatforms());

		layers.get(Layer.FAR_BACKGROUND).getDrawables().clear();
		layers.get(Layer.FAR_BACKGROUND).getDrawables().add(new DrawableTexture(layout.getOutsideBackground(), new Position(0, 0)));
		layers.get(Layer.MAIN_BACKGROUND).getDrawables().clear();
		layers.get(Layer.MAIN_BACKGROUND).getDrawables().add(new DrawableTexture(layout.getInsideBackground(), new Position(0, 0)));

		layers.get(Layer.FOREGROUND).getDrawables().clear();

		for (OverlayInfo info : layout.getTips())
			layers.get(Layer.FOREGROUND).getDrawables().add(new DrawableTexture(info.getWidth(), info.getHeight(), info.getImageName(), info.getPosition()));

		remainingTime = layout.getExpiration();
	}

	public void addEntity(int entId, Entity ent) {
		entities.put(Integer.valueOf(entId), ent);
		layers.get(Layer.MIDGROUND).getDrawables().add(ent);
	}

	public int addEntity(Entity ent) {
		int entId = entities.lastKey().intValue() + 1;
		addEntity(entId, ent);
		return entId;
	}

	public void removeEntity(int entId) {
		Entity removed = entities.remove(Integer.valueOf(entId));
		layers.get(Layer.MIDGROUND).getDrawables().remove(removed);
	}

	public void updateEntityPositions(double tDelta) {
		if (remainingTime <= 0)
			return;
		if (spawner != null && spawner.update(tDelta)) {
			Entity spawned = spawner.getRandomEntity();
			int id = addEntity(spawned);
			if (spawned instanceof Expirable)
				((Expirable)spawned).setEntityId(id);
		}
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

	public double getRemainingTime() {
		return remainingTime;
	}

	public String getName() {
		return name;
	}

	public abstract void drawSpecificDetails(Graphics2D g2d);
}
