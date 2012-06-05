package physics_game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Game1 extends Canvas {
	private enum GameState {
		TITLE_SCREEN, GAME, PAUSE, CREDITS
	}

	private static final long serialVersionUID = -273053717092253478L;
	private static final int FPS = 60;
	public static final int WIDTH = 1280, HEIGHT = 720;

	private NumberFormat FPS_FMT = new DecimalFormat("##");

	private BufferStrategy strategy;

	private final InputHandler controller;
	private final FrameRateState s;
	private GameState state;

	private MainMenuManager titleScreenModel;
	private List<MenuButton> pauseScreenButtons;
	private double remainingBgm;

	private final Camera c;
	private GameMap map;

	private volatile boolean continueGame;
	private volatile CountDownLatch gameRunning;

	public Game1() {
		setFocusable(true);
		requestFocusInWindow();

		controller = new InputHandler();
		addKeyListener(controller);
		addMouseMotionListener(controller);
		addMouseListener(controller);

		map = new GameMap();
		c = new Camera(WIDTH, HEIGHT);
		s = new FrameRateState();

		for (int i = 0; i < 500; i++)
			map.addParticle(new TestParticle());

		gameRunning = new CountDownLatch(0);
		state = GameState.TITLE_SCREEN;

		titleScreenModel = new MainMenuManager(WIDTH, HEIGHT, 1000);
        titleScreenModel.getButtons().add(new MenuButton("New Game", new Rectangle((WIDTH - 200) / 2, HEIGHT / 2, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
	            map.setLevel(LevelCache.getLevel("intro1"));
	            c.setLimits(map.getCameraBounds());
	            c.lookAt(map.getPlayer().getPosition());
	            state = GameState.GAME;
			}
        }));

        pauseScreenButtons = new ArrayList<MenuButton>();
        pauseScreenButtons.add(new MenuButton("New Game", new Rectangle((WIDTH - 200) / 2, 50, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
	            map.setLevel(LevelCache.getLevel("tutorial"));
	            c.setLimits(map.getCameraBounds());
	            c.lookAt(map.getPlayer().getPosition());
	            state = GameState.GAME;
			}
        }));
        pauseScreenButtons.add(new MenuButton("Restart Level", new Rectangle((WIDTH - 200) / 2, 110, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
            map.resetLevel();
            c.setLimits(map.getCameraBounds());
            c.lookAt(map.getPlayer().getPosition());
            state = GameState.GAME;
			}
        }));
        pauseScreenButtons.add(new MenuButton("Main Menu", new Rectangle((WIDTH - 200) / 2, 170, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
            state = GameState.TITLE_SCREEN;
			}
        }));
        pauseScreenButtons.add(new MenuButton("Back to Game", new Rectangle((WIDTH - 200) / 2, 230, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
            state = GameState.GAME;
			}
        }));
	}

	private BufferedImage readImage(String path) throws IOException {
		return ImageIO.read(new BufferedInputStream(getClass().getResourceAsStream(path)));
	}

	private SoundCache.Sound readSound(String path) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		return SoundCache.Sound.read(path, new BufferedInputStream(getClass().getResourceAsStream(path)));
	}

	public void loadContent() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		setIgnoreRepaint(true);

		TextureCache.setTexture("player", readImage("robotBod.png"));
		TextureCache.setTexture("arm", readImage("robotArm.png"));
		BufferedImage texture = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
		int whiteRgb = Color.WHITE.getRGB();
		texture.setRGB(0, 0, whiteRgb);
		texture.setRGB(1, 0, whiteRgb);
		texture.setRGB(2, 0, whiteRgb);
		texture.setRGB(0, 1, whiteRgb);
		texture.setRGB(1, 1, whiteRgb);
		texture.setRGB(2, 1, whiteRgb);
		texture.setRGB(0, 2, whiteRgb);
		texture.setRGB(1, 2, whiteRgb);
		texture.setRGB(2, 2, whiteRgb);
		TextureCache.setTexture("platform", texture);
		TextureCache.setTexture("mainBg", readImage("mainBg.png"));
		TextureCache.setTexture("scrollingWindowBg", readImage("scrollingBg.png"));
		TextureCache.setTexture("mouse", readImage("cursor2.png"));
		TextureCache.setTexture("particletest", readImage("particletest.png"));
		TextureCache.setTexture("box", readImage("crate.png"));
		TextureCache.setTexture("beam", readImage("beam.png"));
		TextureCache.setTexture("door", readImage("door.png"));
		TextureCache.setTexture("openDoor", readImage("opendoor.png"));
		TextureCache.setTexture("switch", readImage("switch.png"));

		TextureCache.setTexture("button", readImage("buttons/buttonRegular.png"));
		TextureCache.setTexture("buttonHover", readImage("buttons/buttonHovering.png"));
		TextureCache.setTexture("buttonPressed", readImage("buttons/buttonPressed.png"));

		TextureCache.setTexture("jetpackOverlay", readImage("overlays/overlayJetpack.png"));
		TextureCache.setTexture("switchOverlay", readImage("overlays/buttonOverlay.png"));
		TextureCache.setTexture("growOverlay", readImage("overlays/growBeamOverlay.png"));
		TextureCache.setTexture("shrinkOverlay", readImage("overlays/shrinkBeamOverlay.png"));
		TextureCache.setTexture("beamOverlay", readImage("overlays/tractorBeamOverlay.png"));
		TextureCache.setTexture("rotateOverlay", readImage("overlays/rotationOverlay.png"));

		TextureCache.setTexture("flame1", readImage("anim_fire/flame1.png"));
		TextureCache.setTexture("flame2", readImage("anim_fire/flame2.png"));
		TextureCache.setTexture("flame3", readImage("anim_fire/flame3.png"));
		TextureCache.setTexture("flame4", readImage("anim_fire/flame4.png"));

		TextureCache.setTexture("legsStand", readImage("anim_legs/robotStand.png"));
		TextureCache.setTexture("legs1", readImage("anim_legs/robotMove1.png"));
		TextureCache.setTexture("legs2", readImage("anim_legs/robotMove2.png"));
		TextureCache.setTexture("legs3", readImage("anim_legs/robotMove3.png"));
		TextureCache.setTexture("legs4", readImage("anim_legs/robotMove4.png"));
		TextureCache.setTexture("legs5", readImage("anim_legs/robotMove5.png"));
		TextureCache.setTexture("rect", readImage("rect.png"));
		TextureCache.setTexture("star", readImage("star.png"));
		TextureCache.setTexture("logo", readImage("logo.png"));
		TextureCache.setTexture("n", readImage("n.png"));
		TextureCache.setTexture("intro1", readImage("cutscene/intro1.png"));
		TextureCache.setTexture("intro2", readImage("cutscene/intro2.png"));
		TextureCache.setTexture("intro3", readImage("cutscene/intro3.png"));
		TextureCache.setTexture("end1", readImage("cutscene/end1.png"));
		TextureCache.setTexture("mid1", readImage("cutscene/mid1.png"));
		TextureCache.setTexture("mid2", readImage("cutscene/mid2.png"));
		SoundCache.setSound("beam", readSound("BeamSound.wav"));
		SoundCache.setSound("grow", readSound("Grow.wav"));
		SoundCache.setSound("shrink", readSound("Shrink.wav"));
		SoundCache.setSound("jetpack", readSound("Jetpack.wav"));
		SoundCache.setSound("bgm", readSound("bgm.wav"));

		LevelCache.initialize();

		setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
	}

	public void unloadContent() {
		TextureCache.flush();
		SoundCache.flush();
	}

	public void stopGame() {
		continueGame = false;
	}

	public void awaitTermination() throws InterruptedException {
		gameRunning.await();
	}

	private void respondToInput() {
		for (Integer key : controller.getCodesOfPressedKeys()) {
			switch (key.intValue()) {
			case KeyEvent.VK_ESCAPE:
				if (!controller.isKeyHeld(KeyEvent.VK_ESCAPE)) {
					switch (state) {
						case GAME:
							state = GameState.PAUSE;
							break;
						case PAUSE:
							state = GameState.GAME;
							break;
					}
					controller.holdKey(KeyEvent.VK_ESCAPE);
				}
				break;
			}
		}
		controller.cleanHeldKeys();
	}

	private void respondToGameInput(double tDelta) {
		for (Integer key : controller.getCodesOfPressedKeys()) {
			switch (key.intValue()) {
			case KeyEvent.VK_W:
				map.getPlayer().move(Direction.UP);
				break;
			case KeyEvent.VK_A:
				map.getPlayer().move(Direction.LEFT);
				break;
			case KeyEvent.VK_S:
				map.getPlayer().move(Direction.DOWN);
				break;
			case KeyEvent.VK_D:
				map.getPlayer().move(Direction.RIGHT);
				break;
			case KeyEvent.VK_1:
				if (map.getSelectedEntity() instanceof StationaryEntity)
					((StationaryEntity) map.getSelectedEntity()).shrink(tDelta);
				break;
			case KeyEvent.VK_2:
				if (map.getSelectedEntity() instanceof StationaryEntity)
					((StationaryEntity) map.getSelectedEntity()).stretch(map.getCollidables(), tDelta);
				break;
			case KeyEvent.VK_3:
				if (map.getSelectedEntity() instanceof StationaryEntity)
					((StationaryEntity) map.getSelectedEntity()).rotate(map.getCollidables(), tDelta);
				break;
			}
		}
	}

	private void updateTitle(double tDelta) {
		titleScreenModel.update(controller, tDelta);
	}

	private void updateGame(double tDelta) {
		respondToGameInput(tDelta);

		if (map.getExitDoor().changeMap() || map.isMapExpired(tDelta)) {
			if (!map.getExitDoor().getDestination().equals("credits")) {
				map.setLevel(LevelCache.getLevel(map.getExitDoor().getDestination()));
				c.setLimits(map.getCameraBounds());
				c.lookAt(map.getPlayer().getPosition());
			} else {
				state = GameState.TITLE_SCREEN;
			}
			return;
		}

		map.updateEntityPositions(tDelta);
		map.updateParticlePositions(tDelta);
		map.cleanParticles();

		Position mousePos = c.mouseToWorld(controller.getMousePosition());
		map.getPlayer().lookAt(map.getSelectedEntity() == null ? mousePos : map.getSelectedEntity().getBeamHit());
		if (controller.getCodesOfPressedMouseButtons().contains(Integer.valueOf(MouseEvent.BUTTON1))) {
			double deltaX = mousePos.getX() - controller.getLastMousePosition().getX();
			double deltaY = mousePos.getY() - controller.getLastMousePosition().getY();
			controller.setLastMousePosition(mousePos);
			if (map.getSelectedEntity() != null) {
				if (!(map.getSelectedEntity() instanceof Switch) && map.getSelectedEntity() instanceof StationaryEntity)
					((StationaryEntity) map.getSelectedEntity()).drag(map.getCollidables(), deltaX, deltaY);
			}
			if (map.getSelectionBeam() == null) {
				map.setSelectionBeam(new Beam(map));
				map.getSelectionBeam().start(map.getPlayer().getGunPosition(), map.getPlayer().getRotation(), map.getPlayer().flipHorizontally());
			} else {
				map.getSelectionBeam().continueBeam(map.getPlayer().getGunPosition(), map.getPlayer().getRotation(), map.getPlayer().flipHorizontally(), tDelta);
			}
		} else {
			if (map.getSelectionBeam() != null) {
				map.getSelectionBeam().finish(map.getPlayer().getGunPosition(), map.getPlayer().getRotation(), map.getPlayer().flipHorizontally(), tDelta);
				if (map.getSelectionBeam().outOfView())
					map.setSelectionBeam(null);
			}
		}

		c.lookAt(map.getPlayer().getPosition());
		List<CollidableDrawable> collidablesList = map.getCollidables();
		CollidableDrawable[] collidables = collidablesList.toArray(new CollidableDrawable[0]);
		for (int i = 0; i < collidables.length - 1; i++) {
			for (int j = i + 1; j < collidables.length; j++) {
				CollisionResult result = PolygonCollision.boundingPolygonCollision(collidables[j].getBoundingPolygon(), collidables[i].getBoundingPolygon());
				if (result.collision()) {
					result.getCollisionInformation().setCollidedWith(collidables[j]);
					collidables[i].collision(result.getCollisionInformation(), collidablesList);
				}
			}
		}
	}

	private void updatePauseOverlay() {
		for (MenuButton btn : pauseScreenButtons) {
			if (btn.isPointInButton(controller.getMousePosition()))
				if (controller.getCodesOfPressedMouseButtons().contains(MouseEvent.BUTTON1) && btn.isMouseDown())
					btn.act();
				else if (controller.getCodesOfPressedMouseButtons().contains(MouseEvent.BUTTON1) && !btn.isMouseDown())
					btn.setMouseDown();
				else if (!controller.getCodesOfPressedMouseButtons().contains(MouseEvent.BUTTON1) && btn.isMouseDown())
					btn.setMouseOver();
				else
					btn.setMouseOver();
			else if (btn.isMouseOver())
				btn.setMouseUp();
		}
	}

	private void updateState(double tDelta) {
		s.addFrame();
		if (s.getElapsedSecondsSinceLastReset() > 1)
			s.reset();

		remainingBgm -= tDelta;
		if (remainingBgm <= 0) {
			SoundCache.getSound("bgm").play();
			remainingBgm = SoundCache.getSound("bgm").getDuration();
		}

		respondToInput();

		switch (state) {
			case TITLE_SCREEN:
				updateTitle(tDelta);
				break;
			case GAME:
				updateGame(tDelta);
				break;
			case PAUSE:
				updatePauseOverlay();
				break;
		}
	}

	private void drawMainMenu(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		titleScreenModel.draw(g2d);
	}

	private void drawGame(Graphics2D g2d) {
		for (Layer layer : map.getLayers().values()) {
			AffineTransform originalTransform = g2d.getTransform();
			g2d.setTransform(c.getViewMatrix(layer.getParallaxFactor()));
			for (AbstractDrawable ent : layer.getDrawables()) {
				g2d.drawImage(ent.getTexture(), ent.getTransformationMatrix(), null);
				if (ent instanceof CollidableDrawable) {
					for (Polygon p : ((CollidableDrawable) ent).getBoundingPolygon().getPolygons()) {
						Point2D[] vertices = p.getVertices();
						int[] xPoints = new int[vertices.length];
						int[] yPoints = new int[vertices.length];
						for (int i = 0; i < vertices.length; i++) {
							xPoints[i] = (int) Math.round(vertices[i].getX());
							yPoints[i] = (int) Math.round(vertices[i].getY());
						}
						g2d.drawPolygon(xPoints, yPoints, vertices.length);
					}
				}
			}

			g2d.setTransform(originalTransform);
		}
	}

	private void drawPauseOverlay(Graphics2D g2d) {
		g2d.setColor(new Color(0, 0, 0, 200));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		for (MenuButton btn : pauseScreenButtons)
			btn.draw(g2d);
	}

	private void paint() {
		do {
			Graphics2D g2d = null;
			do {
				g2d = (Graphics2D) strategy.getDrawGraphics();
				try {
					g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2d.setColor(new Color(100, 149, 237, 255)); //cornflower blue! :P
					g2d.fillRect(0, 0, WIDTH, HEIGHT);

					switch (state) {
						case TITLE_SCREEN:
							g2d.setColor(Color.WHITE);
							drawMainMenu(g2d);
							break;
						default:
							g2d.setColor(Color.BLACK);
							drawGame(g2d);
							if (state == GameState.PAUSE)
								drawPauseOverlay(g2d);
							break;
					}
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("Arial", Font.PLAIN, 16));
					g2d.drawString("FPS: " + FPS_FMT.format(s.getLastCalculatedFps()), 5, g2d.getFontMetrics().getAscent());
					g2d.drawImage(TextureCache.getTexture("mouse"), controller.getMousePosition().x - TextureCache.getTexture("mouse").getWidth() / 2, controller.getMousePosition().y - TextureCache.getTexture("mouse").getHeight() / 2, null);
				} finally {
					g2d.dispose();
				}
			} while (strategy.contentsRestored());
			strategy.show();
			getToolkit().sync();
		} while (strategy.contentsLost());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	public void loop() {
		continueGame = true;
		gameRunning = new CountDownLatch(1);

		long start = System.currentTimeMillis(), lastUpdate = start;
		int frameNum = 0;
		try {
			while (continueGame) {
				long thisUpdate = System.currentTimeMillis();
				updateState((thisUpdate - lastUpdate) / 1000d);
				lastUpdate = thisUpdate;
				paint();

				frameNum++;
				if (frameNum == Integer.MAX_VALUE) {
					start += Math.round((1000d / FPS) * frameNum);
					frameNum = 0;
				}
				Thread.sleep(Math.max(start + Math.round((1000d / FPS) * frameNum) - System.currentTimeMillis(), 0));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			gameRunning.countDown();
		}
	}
}
