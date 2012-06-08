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
import java.util.Iterator;
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
	public static final int WIDTH = 1280, HEIGHT = 720;

	private NumberFormat FPS_FMT = new DecimalFormat("##");

	private BufferStrategy strategy;

	private final InputHandler controller;
	private final FrameRateState s;
	private GameState state;

	private MainMenuManager titleScreenModel;
	private List<MenuButton> pauseScreenButtons;

	private final Camera c;
	private GameMap map;

	private volatile boolean continueGame;
	private volatile CountDownLatch gameRunning;

	private BufferedImage endGameSnapshot;
	private List<ScreenFragment> endGamePieces;

	public Game1() {
		setFocusable(true);
		requestFocusInWindow();

		controller = new InputHandler();
		addKeyListener(controller);
		addMouseMotionListener(controller);
		addMouseListener(controller);

		map = new CannonGameMap();
		c = new Camera(WIDTH, HEIGHT);
		s = new FrameRateState();

		gameRunning = new CountDownLatch(0);
		state = GameState.TITLE_SCREEN;

		titleScreenModel = new MainMenuManager(WIDTH, HEIGHT, 1000);
		titleScreenModel.getButtons().add(new MenuButton("New Game", new Rectangle((WIDTH - 200) / 2, HEIGHT / 2, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
				map.setLevel(LevelCache.getLevel("level1"));
				c.setLimits(map.getCameraBounds());
				c.lookAt(map.getLeftPlayer().getPosition());
				state = GameState.GAME;
			}
		}));

		pauseScreenButtons = new ArrayList<MenuButton>();
		pauseScreenButtons.add(new MenuButton("New Game", new Rectangle((WIDTH - 200) / 2, 50, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
				map.setLevel(LevelCache.getLevel("tutorial"));
				c.setLimits(map.getCameraBounds());
				c.lookAt(map.getLeftPlayer().getPosition());
				state = GameState.GAME;
			}
		}));
		pauseScreenButtons.add(new MenuButton("Restart Level", new Rectangle((WIDTH - 200) / 2, 110, 200, 50), new MenuButton.MenuButtonHandler() {
			@Override
			public void clicked() {
				map.resetLevel();
				c.setLimits(map.getCameraBounds());
				c.lookAt(map.getLeftPlayer().getPosition());
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

	public void loadContent() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		setIgnoreRepaint(true);

		TextureCache.setTexture("bg", readImage("pngBg.png"));
		TextureCache.setTexture("body", readImage("body.png"));
		TextureCache.setTexture("rbody", readImage("rbody.png"));
		TextureCache.setTexture("wheel", readImage("wheel.png"));
		TextureCache.setTexture("blast", readImage("blast.png"));
		TextureCache.setTexture("ball", readImage("ball.png"));
		TextureCache.setTexture("cursor", readImage("cursor.png"));
		TextureCache.setTexture("logo", readImage("logo.png"));
		TextureCache.setTexture("buttonHover", readImage("buttonHovering.png"));
		TextureCache.setTexture("buttonPressed", readImage("buttonPressed.png"));
		TextureCache.setTexture("button", readImage("buttonRegular.png"));
		TextureCache.setTexture("bar", readImage("bar.png"));
		TextureCache.setTexture("greenbar", readImage("greenbar.png"));
		TextureCache.setTexture("rballoon", readImage("rballoon.png"));
		TextureCache.setTexture("gballoon", readImage("gballoon.png"));
		TextureCache.setTexture("bballoon", readImage("bballoon.png"));
		TextureCache.setTexture("pballoon", readImage("pballoon.png"));
		TextureCache.setTexture("neggun", readImage("neggun.png"));
		TextureCache.setTexture("posgun", readImage("posgun.png"));
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
		

		LevelCache.initialize();

		setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
	}

	public void unloadContent() {
		TextureCache.flush();
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
		KeyBindings left = map.getLeftPlayer().getKeyBindings();
		KeyBindings right = map.getRightPlayer().getKeyBindings();

		int lo = 0, ro = 0;
		boolean la = false, ra = false;

		for (Integer key : controller.getCodesOfPressedKeys()) {
			if (key.intValue() == left.upBinding())
				lo++;
			if (key.intValue() == left.downBinding())
				lo--;
			if (key.intValue() == left.actionBinding())
				la = true;

			if (key.intValue() == right.upBinding())
				ro++;
			if (key.intValue() == right.downBinding())
				ro--;
			if (key.intValue() == right.actionBinding())
				ra = true;
		}

		if (map.getLeftPlayer().update(lo, la, tDelta))
			map.getLeftPlayer().triggered(map);
		if (map.getRightPlayer().update(ro, ra, tDelta))
			map.getRightPlayer().triggered(map);
	}

	private void updateTitle(double tDelta) {
		titleScreenModel.update(controller, tDelta);
	}

	private void updateGame(double tDelta) {
		respondToGameInput(tDelta);

		map.updateEntityPositions(tDelta);
		map.updateParticlePositions(tDelta);
		map.cleanParticles();

		if (endGamePieces != null) {
			if (endGamePieces.isEmpty()) {
				if (endGameSnapshot != null) {
					endGameSnapshot.flush();
					endGameSnapshot = null;
				}
			} else {
				for (Iterator<ScreenFragment> iter = endGamePieces.iterator(); iter.hasNext(); ) {
					ScreenFragment text = iter.next();
					if (text.update(Math.random() * Math.PI * 2, 0.3, tDelta))
						iter.remove();
				}
			}
			if (!map.isMapExpired(tDelta)) {
				//switched maps, stop showing screen fragments/endgame screen
				endGamePieces = null;
				if (endGameSnapshot != null) {
					endGameSnapshot.flush();
					endGameSnapshot = null;
				}
			} else {
				return;
			}
		}

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

		List<Integer> toRemove = new ArrayList<Integer>();
		for (Iterator<Entity> iter = map.getEntities().values().iterator(); iter.hasNext(); ) {
			Entity ent = iter.next();
			if (ent instanceof CannonBall) {
				CannonBall ball = (CannonBall) ent;
				if (ball.isLeftPlayer())
					map.getLeftPlayer().addPoints(ball.getPointsSinceLastFrame());
				else
					map.getRightPlayer().addPoints(ball.getPointsSinceLastFrame());
			}
			if (ent instanceof Expirable && ((Expirable) ent).isExpired())
				toRemove.add(Integer.valueOf(((Expirable) ent).getEntityId()));
		}
		for (Integer entId : toRemove)
			map.removeEntity(entId.intValue());

		if (map.isMapExpired(tDelta)) {
			endGameSnapshot = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
			drawGame((Graphics2D) endGameSnapshot.getGraphics());
			endGamePieces = new ArrayList<ScreenFragment>();

			final int COLUMNS = 20, ROWS = 20;
			for (int i = 0; i < COLUMNS; i++)
				for (int j = 0; j < ROWS; j++)
					endGamePieces.add(new ScreenFragment(endGameSnapshot.getSubimage(i * WIDTH / COLUMNS, j * HEIGHT / ROWS, WIDTH / COLUMNS, HEIGHT / ROWS), new Position(i * WIDTH / COLUMNS, j * HEIGHT / ROWS + HEIGHT / ROWS), Math.random() * 2 * Math.PI, 50));
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
		if (endGamePieces != null) {
			int left = map.getLeftPlayer().getPoints(), right = map.getRightPlayer().getPoints();
			String s;
			if (left > right)
				s = "Left player wins!";
			else if (right > left)
				s = "Right player wins!";
			else
				s = "It is a tie!";

			g2d.setFont(new Font("Arial", Font.PLAIN, 72));
			g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT / 2);
			s = (right > left) ? (right + "–" + left) : (left + "–" + right);
			g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT / 2 + g2d.getFontMetrics().getHeight());
			for (ScreenFragment piece : endGamePieces)
				g2d.drawImage(piece.getTexture(), piece.getTransformationMatrix(), null);
			return;
		}
		g2d.setColor(Color.BLACK);
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

		g2d.setFont(new Font("Arial", Font.PLAIN, 36));
		g2d.setColor(Color.BLACK);
		String s = map.getLeftPlayer().getPoints() + "|" + map.getRightPlayer().getPoints();
		g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT - g2d.getFontMetrics().getHeight());

		int milliseconds = (int) (map.getRemainingTime() * 1000);
		long minutes = milliseconds / (1000 * 60);
		long seconds = (milliseconds - (minutes * 1000 * 60)) / 1000;
		s = String.format("%d:%02d", minutes, seconds);
		g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT);
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
					g2d.drawImage(TextureCache.getTexture("cursor"), controller.getMousePosition().x - TextureCache.getTexture("cursor").getWidth() / 2, controller.getMousePosition().y - TextureCache.getTexture("cursor").getHeight() / 2, null);
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

		long lastUpdate = System.currentTimeMillis();
		try {
			while (continueGame) {
				long thisUpdate = System.currentTimeMillis();
				updateState((thisUpdate - lastUpdate) / 1000d);
				lastUpdate = thisUpdate;
				paint();
			}
		} finally {
			gameRunning.countDown();
		}
	}
}
