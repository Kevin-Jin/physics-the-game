package physics_game;

import java.awt.event.KeyEvent;

public class KeyBindings {
	int[] keyCodes;
	
	public KeyBindings(boolean left){
		keyCodes = (left ) ? new int[] { KeyEvent.VK_W,KeyEvent.VK_A,KeyEvent.VK_S,KeyEvent.VK_D,KeyEvent.VK_SPACE}: 
			new int[] {KeyEvent.VK_UP,KeyEvent.VK_LEFT, KeyEvent.VK_DOWN,KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER};
	}
	public int upBinding(){
		return keyCodes[0];
	}
	public int leftBinding(){
		return keyCodes[1];
	}
	public int downBinding(){
		return keyCodes[2];
	}
	public int rightBinding(){
		return keyCodes[3];
	}
	public int actionBinding(){
		return keyCodes[4];
	}

}
