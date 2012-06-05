package physics_game;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InputHandler implements MouseListener, KeyListener, MouseMotionListener {
	private final Point mousePos;
	/**
	 * Needs to be thread-safe since it is written from EDT and read from main thread.
	 */
	private final Set<Integer> pressedKeys;
	/**
	 * Needs to be thread-safe since it is written from EDT and read from main thread.
	 */
	private final Set<Integer> pressedMouseButtons;
	/**
	 * Doesn't need to be thread-safe since we're only accessing it from the main thread.
	 */
	private final Set<Integer> heldKeys;

	private Position lastMousePos;

	public InputHandler() {
		mousePos = new Point(0, 0);
		pressedKeys = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
		pressedMouseButtons = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
		heldKeys = new HashSet<Integer>();
		lastMousePos = new Position();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePos.setLocation(e.getPoint());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos.setLocation(e.getPoint());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys.add(Integer.valueOf(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		pressedMouseButtons.add(Integer.valueOf(e.getButton()));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressedMouseButtons.remove(Integer.valueOf(e.getButton()));
	}

	public Set<Integer> getCodesOfPressedKeys() {
		return pressedKeys;
	}

	public boolean isKeyDown(int keyCode) {
		return pressedKeys.contains(Integer.valueOf(keyCode));
	}

	public Set<Integer> getCodesOfPressedMouseButtons() {
		return pressedMouseButtons;
	}

	public boolean isMouseButtonDown(int buttonId) {
		return pressedMouseButtons.contains(Integer.valueOf(buttonId));
	}

	public Point getMousePosition() {
		return mousePos;
	}

	public boolean isKeyHeld(int key) {
		return heldKeys.contains(Integer.valueOf(key));
	}

	public void holdKey(int key) {
		heldKeys.add(Integer.valueOf(key));
	}

	public void cleanHeldKeys() {
		Set<Integer> stillHeld = new HashSet<Integer>();
		for (Integer key : heldKeys)
			if (isKeyDown(key.intValue()))
				stillHeld.add(key);
		heldKeys.clear();
		heldKeys.addAll(stillHeld);
	}

	public Position getLastMousePosition() {
		return lastMousePos;
	}

	public void setLastMousePosition(Position p) {
		lastMousePos = p;
	}
}
