package physics_game;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class GameMap {
	private static final int FLOOR_VISIBLE_PIXELS = 0;
	private static final int CEILING_VISIBLE_PIXELS = 0;
	private static final int LEFT_WALL_VISIBLE_PIXELS = 0;
	private static final int RIGHT_WALL_VISIBLE_PIXELS = 0;

	private LevelLayout layout;
	private final Player player;
	private final SortedMap<Byte, Entity> entities;
	private final List<Particle> particles;
	private final SortedMap<Byte, Layer> layers;

	private ExitDoor door;
	private Beam beam;
	private CollidableDrawable draggedEnt;
	private double remainingTime;

	public GameMap() {
		player = new Player();
		entities = new TreeMap<Byte, Entity>();
		particles = new ArrayList<Particle>();
		layers = new TreeMap<Byte, Layer>();

		layers.put(Layer.FAR_BACKGROUND, new Layer(0.25));
		layers.put(Layer.MAIN_BACKGROUND, new Layer(0.5));
		layers.put(Layer.MIDGROUND, new Layer(1));
		layers.put(Layer.FOREGROUND, new Layer(2));
	}

	public ExitDoor getExitDoor() {
		return door;
	}

	public Player getPlayer() {
		return player;
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

	public Beam getSelectionBeam() {
		return beam;
	}

	public void setSelectionBeam(Beam value) {
		beam = value;
		if (Double.isInfinite(remainingTime)) {
			if (beam != null)
				addEntity((byte) 5, beam);
			else
				removeEntity((byte) 5);
		}
	}

	public CollidableDrawable getSelectedEntity() {
		return draggedEnt;
	}

	public void setSelectedEntity(CollidableDrawable value) {
		if (draggedEnt != null)
			if (draggedEnt instanceof StationaryEntity)
				((StationaryEntity) draggedEnt).setHeld(false);
		if (value != null)
			if (!(value instanceof Switch) && value instanceof StationaryEntity)
				((StationaryEntity) value).setHeld(true);
		draggedEnt = value;
	}

	public void setLevel(LevelLayout layout) {
		this.layout = layout;
		door = new ExitDoor(layout.getEndPosition(), layout.getNextMap());

		entities.clear();
		beam = null;
		draggedEnt = null;

		layers.get(Layer.MIDGROUND).getDrawables().clear();
		layers.get(Layer.MIDGROUND).getDrawables().addAll(layout.getPlatforms());

		layers.get(Layer.FAR_BACKGROUND).getDrawables().clear();
		layers.get(Layer.FAR_BACKGROUND).getDrawables().add(new DrawableTexture(layout.getOutsideBackground(), new Position(0, 0)));
		layers.get(Layer.MAIN_BACKGROUND).getDrawables().clear();
		layers.get(Layer.MAIN_BACKGROUND).getDrawables().add(new DrawableTexture(layout.getInsideBackground(), new Position(0, 0)));

		layers.get(Layer.FOREGROUND).getDrawables().clear();

		if (Double.isInfinite(layout.getExpiration())) {
			player.setPosition(layout.getStartPosition());
			addEntity((byte) 0, door);
			addEntity((byte) 1, player.getLeg());
			addEntity((byte) 2, player.getBody());
			addEntity((byte) 3, player.getArm());
			addEntity((byte) 4, player.getJetPackFire());
		}
		// reserve entity id 5 for beam
		byte i = 6;
		for (BoxSpawnInfo info : layout.getBoxes())
			addEntity(i++, new Box(info.getPosition(), info.getStartScale(), info.getMinimumScale(), info.getMaximumScale()));
		for (RectangleSpawnInfo info : layout.getRectangles())
			addEntity(i++, new RectangleBox(info.getPosition(), info.getStartScale(), info.getMinimumScale(), info.getMaximumScale()));
		for (NBoxSpawnInfo info : layout.getNBoxes())
			addEntity(i++, new NBox(info.getPosition()));
		for (SwitchSpawnInfo info : layout.getSwitches())
			addEntity(i++, new Switch(info.getColor(), info.getPosition(), info.getSwitchables()));
		for (OverlayInfo info : layout.getTips())
			layers.get(Layer.FOREGROUND).getDrawables().add(new DrawableTexture(info.getWidth(), info.getHeight(), info.getImageName(), info.getPosition()));
		for (RetractablePlatform retractablePlat : layout.getDoors())
			retractablePlat.reset();

		remainingTime = layout.getExpiration();
	}

	public void resetLevel() {
		setLevel(layout);
	}

	public void addEntity(byte entId, Entity ent) {
		entities.put(Byte.valueOf(entId), ent);
		layers.get(Layer.MIDGROUND).getDrawables().add(ent);
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
