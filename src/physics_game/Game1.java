package physics_game;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
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
		OVERVIEW,  GAME, PAUSE, GAME_OVER
	}

	private static final long serialVersionUID = -273053717092253478L;
	public static final int WIDTH = 1280, HEIGHT = 720;
	public static final double METERS_PER_PIXEL = 1d / 100;
	private static final int BUTTON_WIDTH = WIDTH / 10, BUTTON_HEIGHT = HEIGHT / 10;
	public static final String LEFT_TEAM_NAME = "Black", RIGHT_TEAM_NAME = "White";

	private NumberFormat FPS_FMT = new DecimalFormat("##");

	private BufferStrategy strategy;

	private final InputHandler controller;
	private final FrameRateState s;
	private GameState state;

	private final MainMenuManager titleScreenModel;
	private final List<Button> pauseScreenButtons;
	private final List<Button> gameOverButtons;

	private final Camera c;
	private GameMap map;

	private volatile boolean continueGame;
	private volatile CountDownLatch gameRunning;

	private BufferedImage endGameSnapshot;
	private List<ScreenFragment> endGamePieces;
	private int leftTeamWins, rightTeamWins;
	private int leftTeamPoints, rightTeamPoints;
	private final String[] gameOrder = { "Electron Invaders", "Tank", "The Awesome Game" };
	private int currentGameIndex;

	private CustomImageButton makeGamePreviewButton(final String mapName) {
		int x = (WIDTH - BUTTON_WIDTH) / 2, y = BUTTON_HEIGHT * 2;
		GameMap originalMap = map;
		map = MapCache.getMap(mapName);
		map.resetLevel();
		c.setLimits(map.getCameraBounds());
		c.lookAt(new Position(0, 0));
		map.updateEntityPositions(0);
		try {
			return new CustomImageButton(gameScreenShot(), "Next Game:\n" + mapName, new Rectangle(x, y, BUTTON_WIDTH, BUTTON_HEIGHT), new MenuButton.MenuButtonHandler() {
				@Override
				public void clicked() {
					map = new InfoScreenGameMap(mapName);
					map.resetLevel();
					c.setLimits(map.getCameraBounds());
					c.lookAt(new Position(0, 0));
					state = GameState.GAME;
				}
			});
		} finally {
			map = originalMap;
		}
	}

	public Game1() {
		setFocusable(true);
		requestFocusInWindow();
		controller = new InputHandler();
		addKeyListener(controller);
		addMouseMotionListener(controller);
		addMouseListener(controller);
		c = new Camera(WIDTH, HEIGHT);
		s = new FrameRateState();

		gameRunning = new CountDownLatch(0);
		state = GameState.OVERVIEW;

		titleScreenModel = new MainMenuManager(WIDTH, HEIGHT, 1000);
		pauseScreenButtons = new ArrayList<Button>();
		gameOverButtons = new ArrayList<Button>();
	}

	private BufferedImage readImage(String path) throws IOException {
		return ImageIO.read(new BufferedInputStream(getClass().getResourceAsStream(path)));
	}

	public void loadContent() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		setIgnoreRepaint(true);

		TextureCache.setTexture("pongBG", readImage("pngBg.png"));
		TextureCache.setTexture("cannonBG", readImage("cannonBackground.png"));
		TextureCache.setTexture("starBG", readImage("stars.png"));
		TextureCache.setTexture("pongInfo", readImage("tank.png"));
		TextureCache.setTexture("cannonInfo", readImage("AwesomeGame.png"));
		TextureCache.setTexture("chargesInfo", readImage("electronInvaders.png"));
		
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
		TextureCache.setTexture("star_pos", readImage("star_pos.png"));
		TextureCache.setTexture("star_neg", readImage("star_neg.png"));
		TextureCache.setTexture("paddle",readImage("pongPaddle.png"));
		TextureCache.setTexture("wave",readImage("wave.png"));
		TextureCache.setTexture("rasteroid", readImage("rasteroid.png"));
		TextureCache.setTexture("basteroid", readImage("basteroid.png"));
		TextureCache.setTexture("refraction", readImage("refraction.png"));
		TextureCache.setTexture("chest", readImage("redchest.png"));
		TextureCache.setTexture("pillar", readImage("pillar.png"));
		for (int i =0; i < 8; ++i)
		TextureCache.setTexture("particletest" + i, readImage("particletest" + i + ".png"));
		
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

		MapCache.initialize();

		setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));

		titleScreenModel.getButtons().add(0, new MenuButton("Reset Results", new Rectangle((WIDTH - BUTTON_WIDTH) / 2, BUTTON_HEIGHT * 3, BUTTON_WIDTH, BUTTON_HEIGHT), new Button.MenuButtonHandler() {
			@Override
			public void clicked() {
				leftTeamWins = rightTeamWins = 0;
				leftTeamPoints = rightTeamPoints = 0;
				titleScreenModel.getButtons().subList(3, titleScreenModel.getButtons().size()).clear();
				currentGameIndex = 0;
				titleScreenModel.getButtons().set(2, makeGamePreviewButton(gameOrder[currentGameIndex]));
				endGamePieces = null;
			}
		}));
		titleScreenModel.getButtons().add(1, new MenuButton("Exit Game", new Rectangle((WIDTH - BUTTON_WIDTH) / 2, BUTTON_HEIGHT * 4, BUTTON_WIDTH, BUTTON_HEIGHT), new Button.MenuButtonHandler() {
			@Override
			public void clicked() {
				System.exit(0);
			}
		}));
		titleScreenModel.getButtons().add(2, makeGamePreviewButton(gameOrder[currentGameIndex]));

		pauseScreenButtons.add(new MenuButton("Exit to Overview", new Rectangle((WIDTH - 200) / 2, 50, 200, 50), new Button.MenuButtonHandler() {
			@Override
			public void clicked() {
				state = GameState.OVERVIEW;
			}
		}));
		pauseScreenButtons.add(new MenuButton("Back to Game", new Rectangle((WIDTH - 200) / 2, 110, 200, 50), new Button.MenuButtonHandler() {
			@Override
			public void clicked() {
				state = GameState.GAME;
			}
		}));

		gameOverButtons.add(new MenuButton("Play again", new Rectangle((WIDTH - 200) / 2, 500, 200, 50), new Button.MenuButtonHandler() {
			@Override
			public void clicked() {
				leftTeamWins = rightTeamWins = 0;
				leftTeamPoints = rightTeamPoints = 0;
				titleScreenModel.getButtons().subList(3, titleScreenModel.getButtons().size()).clear();
				currentGameIndex = 0;
				titleScreenModel.getButtons().set(2, makeGamePreviewButton(gameOrder[currentGameIndex]));
				endGamePieces = null;
				state = GameState.OVERVIEW;
			}
		}));
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

	private void updateTitle(double tDelta) {
		titleScreenModel.update(controller, tDelta);
		if (endGamePieces != null) {
			for (Iterator<ScreenFragment> iter = endGamePieces.iterator(); iter.hasNext(); ) {
				ScreenFragment text = iter.next();
				if (text.update(Math.random() * Math.PI * 2, 0.3, tDelta))
					iter.remove();
			}
		}
	}

	private BufferedImage gameScreenShot() {
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
		drawGame((Graphics2D) img.getGraphics());
		return img;
	}

	private void updateGame(double tDelta) {
		map.respondToInput(controller.getCodesOfPressedKeys(), tDelta);

		map.updateEntityPositions(tDelta);
		map.updateParticlePositions(tDelta);
		map.cleanParticles();

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
			if (map.getNextMap() != null) {
				map = MapCache.getMap(map.getNextMap());
				map.resetLevel();
				c.setLimits(map.getCameraBounds());
				c.lookAt(new Position(0, 0));
			} else {
				endGameSnapshot = gameScreenShot();
				endGamePieces = new ArrayList<ScreenFragment>();

				leftTeamPoints = map.getLeftPlayer().getPoints();
				rightTeamPoints = map.getRightPlayer().getPoints();
				int x = 0, y = 0;
				if (leftTeamPoints > rightTeamPoints) {
					leftTeamWins++;
					x = (WIDTH / 3 - BUTTON_WIDTH) / 2;
					y = HEIGHT - BUTTON_HEIGHT - leftTeamWins * BUTTON_HEIGHT;
				} else if (rightTeamPoints > leftTeamPoints) {
					rightTeamWins++;
					x = (WIDTH * 5 / 3 - BUTTON_WIDTH) / 2;
					y = HEIGHT - BUTTON_HEIGHT - rightTeamWins * BUTTON_HEIGHT;
				}
				if (leftTeamPoints != rightTeamPoints) {
					titleScreenModel.getButtons().add(new CustomImageButton(endGameSnapshot, map.getName(), new Rectangle(x, y, BUTTON_WIDTH, BUTTON_HEIGHT), new MenuButton.MenuButtonHandler() {
						@Override
						public void clicked() {

						}
					}));
				}
				currentGameIndex = (currentGameIndex + 1) % gameOrder.length;
				titleScreenModel.getButtons().set(2, makeGamePreviewButton(gameOrder[currentGameIndex]));

				final int COLUMNS = 20, ROWS = 20;
				for (int i = 0; i < COLUMNS; i++)
					for (int j = 0; j < ROWS; j++)
						endGamePieces.add(new ScreenFragment(endGameSnapshot.getSubimage(i * WIDTH / COLUMNS, j * HEIGHT / ROWS, WIDTH / COLUMNS, HEIGHT / ROWS), new Position(i * WIDTH / COLUMNS, j * HEIGHT / ROWS + HEIGHT / ROWS), Math.random() * 2 * Math.PI, 50));

				if (leftTeamWins == 8 || rightTeamWins == 8) {
					map = MapCache.getMap("congratulations");
					map.resetLevel();
					state = GameState.GAME_OVER;
				} else {
					state = GameState.OVERVIEW;
				}
			}
		}
	}

	private void updatePauseOverlay() {
		for (Button btn : pauseScreenButtons) {
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

	private void updateCongratulations(double tDelta) {
		map.updateEntityPositions(tDelta);
		map.updateParticlePositions(tDelta);
		map.cleanParticles();
		for (Button btn : gameOverButtons) {
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
			case OVERVIEW:
				updateTitle(tDelta);
				break;
			case GAME:
				updateGame(tDelta);
				break;
			case PAUSE:
				updatePauseOverlay();
				break;
			case GAME_OVER:
				updateCongratulations(tDelta);
				break;
		}
	}

	private void drawMainMenu(Graphics2D g2d) {
		g2d.drawImage(TextureCache.getTexture("pillar"), (WIDTH - TextureCache.getTexture("pillar").getWidth()) / 2, BUTTON_HEIGHT, null);
		g2d.drawImage(TextureCache.getTexture("chest"), (WIDTH - TextureCache.getTexture("chest").getWidth()) / 2, BUTTON_HEIGHT - TextureCache.getTexture("chest").getHeight(), null);
		titleScreenModel.draw(g2d);
		if (endGamePieces != null) {
			String s;
			if (leftTeamPoints > rightTeamPoints)
				s = LEFT_TEAM_NAME + " team wins!";
			else if (rightTeamPoints > leftTeamPoints)
				s = RIGHT_TEAM_NAME + " team wins!";
			else
				s = "It is a tie!";

			g2d.setFont(new Font("Arial", Font.PLAIN, 72));
			g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT - g2d.getFontMetrics().getDescent());
			for (ScreenFragment piece : endGamePieces)
				g2d.drawImage(piece.getTexture(), piece.getTransformationMatrix(), null);
		}
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		String s = "Games won by " + LEFT_TEAM_NAME + " team";
		g2d.drawString(s, (WIDTH / 3 - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT - BUTTON_HEIGHT + g2d.getFontMetrics().getHeight());
		s = "Goal";
		g2d.drawString(s, (WIDTH / 3 - g2d.getFontMetrics().stringWidth(s)) / 2, BUTTON_HEIGHT);
		s = "Games won by " + RIGHT_TEAM_NAME + " team";
		g2d.drawString(s, (WIDTH * 5 / 3 - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT - BUTTON_HEIGHT + g2d.getFontMetrics().getHeight());
		s = "Goal";
		g2d.drawString(s, (WIDTH * 5 / 3 - g2d.getFontMetrics().stringWidth(s)) / 2, BUTTON_HEIGHT);
	}

	private void drawMapEntities(Graphics2D g2d) {
		for (Layer layer : map.getLayers().values()) {
			AffineTransform originalTransform = g2d.getTransform();
			g2d.setTransform(c.getViewMatrix(layer.getParallaxFactor()));
			for (AbstractDrawable ent : layer.getDrawables()) {
				if (ent.getAlpha() != 1) {
					Composite originalComposite = g2d.getComposite();
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ent.getAlpha()));
					g2d.drawImage(ent.getTexture(), ent.getTransformationMatrix(), null);
					g2d.setComposite(originalComposite);
				} else {
					g2d.drawImage(ent.getTexture(), ent.getTransformationMatrix(), null);
				}
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

	private void drawGame(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		drawMapEntities(g2d);
		String s = null;

		g2d.setFont(new Font("Arial", Font.PLAIN, 36));
		g2d.setColor(Color.BLACK);
		if (map.shouldDrawPlayerDetail()){
		 s = map.getLeftPlayer().getPoints() + "|" + map.getRightPlayer().getPoints();
		g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT - g2d.getFontMetrics().getHeight());
		
		int milliseconds = (int) (map.getRemainingTime() * 1000);
		long minutes = milliseconds / (1000 * 60);
		long seconds = (milliseconds - (minutes * 1000 * 60)) / 1000;
		s = String.format("%d:%02d", minutes, seconds);
		g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, HEIGHT);

		final int SCALE_THICKNESS = 5, SCALE_EDGE_HEIGHT = 15, PADDING = 10;
		g2d.fillRect(PADDING, HEIGHT - SCALE_EDGE_HEIGHT - PADDING, SCALE_THICKNESS, SCALE_EDGE_HEIGHT);
		g2d.fillRect(PADDING, HEIGHT - PADDING - SCALE_THICKNESS, (int) (1 / METERS_PER_PIXEL), SCALE_THICKNESS);
		g2d.fillRect((int) (1 / METERS_PER_PIXEL) + PADDING - SCALE_THICKNESS, HEIGHT - SCALE_EDGE_HEIGHT - PADDING, SCALE_THICKNESS, SCALE_EDGE_HEIGHT);
		s = "1m";
		g2d.drawString(s, (int) (1 / METERS_PER_PIXEL) / 2 - g2d.getFontMetrics().stringWidth(s) / 2 + PADDING, HEIGHT - PADDING - SCALE_THICKNESS - g2d.getFontMetrics().getDescent());
		}
		map.drawSpecificDetails(g2d);
	}

	private void drawPauseOverlay(Graphics2D g2d) {
		g2d.setColor(new Color(0, 0, 0, 200));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		for (Button btn : pauseScreenButtons)
			btn.draw(g2d);
	}

	private void drawCongratulations(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 72));
		String s = "Congratulations " + (leftTeamWins > rightTeamWins ? LEFT_TEAM_NAME : RIGHT_TEAM_NAME) + " team!";
		g2d.drawString(s, (WIDTH - g2d.getFontMetrics().stringWidth(s)) / 2, (HEIGHT - g2d.getFontMetrics().getHeight()) / 2);
		for (Button btn : gameOverButtons)
			btn.draw(g2d);

		drawMapEntities(g2d);
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
						case OVERVIEW:
							g2d.setColor(Color.WHITE);
							drawMainMenu(g2d);
							break;
						case GAME_OVER:
							drawCongratulations(g2d);
							break;
						default:
							g2d.setColor(Color.BLACK);
							drawGame(g2d);
							if (state == GameState.PAUSE)
								drawPauseOverlay(g2d);
							break;
					}
					g2d.setColor(Color.BLACK);
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
