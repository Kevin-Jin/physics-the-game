package physics_game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class GameMap {
	private final int bottomPadding;
	private final int topPadding;
	private final int leftPadding;
	private final int rightPadding;

	private final String name;
	protected final LevelLayout layout;
	private final SortedMap<Integer, Entity> entities;
	protected final List<Particle> particles;
	protected final SortedMap<Byte, Layer> layers;

	protected List<Spawner<?>> spawners;

	private double remainingTime;

	public GameMap(String name, LevelLayout layout, Spawner<?> spawner) {
		this(name, layout, Collections.<Spawner<?>>singletonList(spawner));
	}

	public GameMap(String name, LevelLayout layout, int bottomPadding) {
		this.name = name;
		this.layout = layout;
		spawners = Collections.emptyList();
		entities = new TreeMap<Integer, Entity>();
		particles = new ArrayList<Particle>();
		layers = new TreeMap<Byte, Layer>();
		layers.put(Layer.FAR_BACKGROUND, new Layer(0.25));
		layers.put(Layer.MAIN_BACKGROUND, new Layer(0.5));
		layers.put(Layer.MIDGROUND, new Layer(1));
		layers.put(Layer.FOREGROUND, new Layer(2));
		this.bottomPadding = bottomPadding;
		topPadding = leftPadding = rightPadding = 0;
	}

	public GameMap(String name, LevelLayout layout, List<Spawner<?>> spawners) {
		this.name = name;
		this.layout = layout;
		this.spawners = spawners;
		entities = new TreeMap<Integer, Entity>();
		particles = new ArrayList<Particle>();
		layers = new TreeMap<Byte, Layer>();
		layers.put(Layer.FAR_BACKGROUND, new Layer(0.25));
		layers.put(Layer.MAIN_BACKGROUND, new Layer(0.5));
		layers.put(Layer.MIDGROUND, new Layer(1));
		layers.put(Layer.FOREGROUND, new Layer(2));
		bottomPadding = 80;
		topPadding = leftPadding = rightPadding = 0;
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
		return new Rectangle(-leftPadding, -bottomPadding, layout.getWidth() + leftPadding + rightPadding, layout.getHeight() + bottomPadding + topPadding);
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

	public String getNextMap() {
		return layout.getNextMap();
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
		for (Spawner<?> spawner : spawners){
			if (spawner.update(tDelta)) {
				Entity spawned = spawner.getRandomEntity();
				int id = addEntity(spawned);
				if (spawned instanceof Expirable)
					((Expirable)spawned).setEntityId(id);
			}
		}
		for (Entity ent : entities.values())
			ent.recalculate(getCollidables(), 0, layout.getGravitationalFieldStrength(), layout.getTerminalVelocity(), tDelta);
	}

	public void respondToInput(Set<Integer> keys, double tDelta){
		int lx = 0, rx = 0, ly = 0, ry = 0;
		boolean la = false, ra = false;
		KeyBindings left =getLeftPlayer().getKeyBindings(), right = getRightPlayer().getKeyBindings();

		for (Integer key : keys) {
			if (key.intValue() == left.rightBinding())
				lx++;
			if (key.intValue() == left.leftBinding())
				lx--;
			if (key.intValue() == left.upBinding())
				ly++;
			if (key.intValue() == left.downBinding())
				ly--;
			if (key.intValue() == left.actionBinding())
				la = true;
			

			if (key.intValue() == right.rightBinding())
				rx++;
			if (key.intValue() == right.leftBinding())
				rx--;
			if (key.intValue() == right.upBinding())
				ry++;
			if (key.intValue() == right.downBinding())
				ry--;
			if (key.intValue() == right.actionBinding())
				ra = true;
		}

		if (getLeftPlayer().update(lx,ly, la, tDelta))
			getLeftPlayer().triggered(this);
		if (getRightPlayer().update(rx,ry, ra, tDelta))
			getRightPlayer().triggered(this);
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
	public boolean shouldDrawPlayerDetail(){
		return true;
	}

	public String getName() {
		return name;
	}

	public abstract void drawSpecificDetails(Graphics2D g2d);
}
