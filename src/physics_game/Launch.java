package physics_game;

import java.applet.Applet;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

@SuppressWarnings("serial")
public class Launch extends Applet {
	private static final boolean FULL_SCREEN = true;

	private Game1 t;

	@Override
	public void init() {
		t = new Game1();
		add(t);

		try {
			t.loadContent();
		} catch (IOException e) {
			System.err.println("Could not load content!");
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			System.err.println("Could not load content!");
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			System.err.println("Could not load content!");
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				t.loop();
			}
		}, "gameloop").start();
	}

	@Override
	public void stop() {
		t.stopGame();
		try {
			t.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		t.unloadContent();
	}

	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		System.setProperty("sun.awt.noerasebackground", "true");
		//System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.noddraw", "false");
		System.setProperty("sun.java2d.d3d", "true");

		final Game1 t = new Game1();
		Frame frame = new Frame("TSA Game");

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				t.stopGame();
			}
		});

		try {
			frame.setResizable(false);
			frame.add(t);
			if (FULL_SCREEN) {
				frame.setUndecorated(true);
				GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				gd.setFullScreenWindow(frame);
				gd.setDisplayMode(new DisplayMode(Game1.WIDTH, Game1.HEIGHT, 32, 60));

				t.loadContent();

				frame.validate();
			} else {
				frame.pack();

				t.loadContent();

				frame.setVisible(true);
			}
			Thread.currentThread().setName("gameloop");
			t.loop();
		} finally {
			t.unloadContent();
			frame.dispose();
		}
	}
}
