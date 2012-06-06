package physics_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutionException;

import javax.sound.sampled.Clip;
import javax.swing.JApplet;


public class Pong extends JApplet implements Runnable
{
	private Clip soundPaddleHit, soundBoundsHit, soundOutOfBounds;
	//private SoundPlayer getSound1, getSound2, getSound3;
	private static final long serialVersionUID = 1L;
	private final Font scoresFont = new Font("Arial",Font.PLAIN,20);
	private final Font titleFont = new Font("Arial",Font.BOLD,26);
	private final int play1X = 6, play2X = 582;
	private final int boardHeight = 55, boardWidth = 10;
	private final int radius = 4;
	private int width, height;
	private Ball gameball;
	private Point ballDefault;
	private boolean gameStarted = false, isPaused = false, sounds = true;
	private int play1Score = 0, play2Score = 0, frames;
	private boolean play1Turn, play2Turn;
	private long start;
	private Graphics screen;
	private Graphics2D buffer, titleBuffer;
	private Thread animator;
	private Image offImage, titleImage;
	private Rectangle p1, p2;
	private int play1Y, play2Y, play1dy, play2dy;

	@Override
	public void init()
	{
		this.setLayout(null);
		this.setSize(600,400);
		this.setSize(new Dimension(600,400));
		screen = this.getGraphics();
		screen.setColor(Color.black);
		this.setBackground(Color.black);
		play1Y = play2Y = (this.getHeight()-boardHeight)/2;
		this.addKeyListener(new KeyMovement());
		this.addMouseListener(new MouseList());
		play1dy = 0;
		play2dy = 0;
		p1 = new Rectangle(play1X, play1Y,boardWidth,boardHeight);
		p2 = new Rectangle(play2X,play2Y,boardWidth,boardHeight);
		ballDefault = new Point (this.getWidth()/2,this.getHeight()/2);
		gameball = new Ball(ballDefault,radius,1,this.getHeight()-2);
		/*getSound1 = new SoundPlayer(getDocumentBase(),"Pong_4360.wav");
		getSound2 = new SoundPlayer(getDocumentBase(),"Pong_4359.wav");
		getSound3 = new SoundPlayer(getDocumentBase(),"Pong_4386.wav");
		getSound1.execute();
		getSound2.execute();
		getSound3.execute();*/
		frames = 0;
		play1Turn = false;
		play2Turn = !play1Turn;
		width = this.getWidth();
		height = this.getHeight();
		/*long startSound = System.currentTimeMillis();
		try {
			soundPaddleHit = getSound1.get();
			soundBoundsHit = getSound2.get();
			soundOutOfBounds = getSound3.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			System.out.println(System.currentTimeMillis()-startSound);
		}*/
	}

	@Override
	public void start()
	{
		if (animator == null)
			animator = new Thread(this);
		animator.start();
	}
	@Override
	public void stop()
	{
		long end = System.currentTimeMillis();
		if (animator != null)
			animator = null;
		System.out.println("Time: (Ms) " + (end-start));
		System.out.println("FPS: " + (frames*1000.0/(end-start)));
		System.out.println("FPS does NOT account for pauses!");
	}
	//Main Game Loop
	public void run()
	{
		this.requestFocusInWindow();
		while (animator != null && Thread.currentThread() == animator)
		{
			//Make sure the game has actually started
			if (gameStarted)
			{
				//Make sure the game is paused otherwise
				// there is no need to update objects
				if(!isPaused)
				{	
					checkCollisions();
					gameball.update();
					updatePanels();
					repaint();
					resetSounds();
				}
				else
					repaintPausedScreen();
			}
			//Syncs the screen with the latest repaint
			this.getToolkit().sync();
			try {
				//Pauses (sleeps) for 9 milliseconds
				Thread.sleep(9);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void repaint()
	{
		//If no buffer currently exists, create one
		//and draw the white outline
		if (buffer == null || offImage == null)
		{
			offImage = this.createImage(600, 400);
			buffer = (Graphics2D)offImage.getGraphics();
			buffer.setFont(scoresFont);
			buffer.setColor(Color.white);
			buffer.drawLine(0,0,599,0);
			buffer.drawLine(0,0, 0, 399);
			buffer.drawLine(0, 399, 599, 399);
			buffer.drawLine(599,0,599,399);
		}
		//Draw everything to the buffer
		buffer.setColor(Color.black);
		buffer.fillRect(1,1,598,398);
		buffer.setColor(Color.white);
		buffer.drawString(""+play1Score, 200, 20);
		buffer.drawString(""+play2Score,400,20);
		buffer.fill(p1);
		buffer.fill(p2);
		buffer.fillOval(gameball.getX()-radius,gameball.getY()-radius , radius*2,radius*2);
		//Draw the image to the screen
		screen.drawImage(offImage, 0,0,600,400,this);
		//Increase frame count
		frames++;
	}
	private void repaintPausedScreen()
	{
		if (buffer != null)
			buffer.drawString("Game Paused", (width-100)/2, (height-10)/2);
		screen.drawImage(offImage, 0,0,600,400,this);
	}
	private void updatePanels()
	{
		play1Y += play1dy;
		play1Y = Math.max(2, play1Y);
		play1Y = Math.min(play1Y, 398-boardHeight);
		play2Y +=play2dy;
		play2Y = Math.max(2, play2Y);
		play2Y = Math.min(play2Y, 398-boardHeight);
		p1.setLocation(play1X, play1Y);
		p2.setLocation(play2X,play2Y);
	}
	private void checkCollisions()
	{
		//If it hits the horizontal boundaries
		if (gameball.collidesWithLinesY())
		{
			if(sounds && soundBoundsHit != null)
			{
				soundBoundsHit.start();
			}
		}
		//If it hits Player 1's paddle
		 if (play1Turn && gameball.collidesWithPadle(p1))
		{
			 if(sounds && soundPaddleHit != null)
				{
				 soundPaddleHit.start();
				}
			//Switches checking "turns" 
			play1Turn = !play1Turn;
			play2Turn = !play2Turn;
			gameball.increaseSpeed();
			//If invalid hit (top/bottom of paddle)
			if (gameball.getRectXCord() < play1X+boardWidth-2)
				gameball.flipVerticalDirection();
			else	//If valid hit (right side of paddle)
				gameball.changeDeltasBasedOnRect(true);
		}
		//If it hits Player 2's paddle
		 if (play2Turn && gameball.collidesWithPadle(p2))
		{
			 if(sounds && soundPaddleHit != null)
				{
				 soundPaddleHit.start();
				}
			//Switches checking "turns" 
			play1Turn = !play1Turn;
			play2Turn = !play2Turn;
			gameball.increaseSpeed();
			//If invalid hit (top/bottom of paddle)
			if (gameball.getRectXCord() > play2X+2)
				gameball.flipVerticalDirection();
			else	//If valid hit (right side of paddle)
				gameball.changeDeltasBasedOnRect(false);
		}
		//If ball is out of bounds
		 if (gameball.getX() <= 0 || gameball.getX() >= 599)
		{
			 if(sounds && soundOutOfBounds != null)
				{
				 soundOutOfBounds.start();
				}
			//If player 2 has scored
			if (gameball.getX()<= 0)
			{
				play2Score++;
				gameball.reset();
				gameball.resetDxDy(false);
				play1Turn = true;
				play2Turn = !play1Turn;
			}
			else //if player 1 has scored
			{
				play1Score++;
				gameball.reset();
				gameball.resetDxDy(true);
				play1Turn = false;
				play2Turn = !play1Turn;
			}
			//Bring panels back to the middle of the screen
			play1Y = play2Y = (this.getHeight()-boardHeight)/2;
			//Pause for half of a second
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void resetSounds()
	{
		if (sounds)
		{
		if (soundBoundsHit != null && !soundBoundsHit.isRunning())
			soundBoundsHit.setFramePosition(0);
		if(soundPaddleHit != null && !soundPaddleHit.isRunning())
			soundPaddleHit.setFramePosition(0);
		if(soundOutOfBounds != null && !soundOutOfBounds.isRunning())
			soundOutOfBounds.setFramePosition(0);
		}
	}
	public void paint(Graphics g)
	{ 	
		if (!gameStarted)//If game has not started, use title image
		{
			//If title image has not been created yet, create it
			if (this.titleBuffer == null || this.titleImage == null)
			{
				titleImage = this.createImage(600, 400);
				titleBuffer = (Graphics2D)titleImage.getGraphics();
				titleBuffer.setColor(Color.black);
				titleBuffer.fillRect(1,1,598,398);
				titleBuffer.setColor(Color.white);
				titleBuffer.drawLine(0,0,599,0);
				titleBuffer.drawLine(0,0, 0, 399);
				titleBuffer.drawLine(0, 399, 599, 399);
				titleBuffer.drawLine(599,0,599,399);
				titleBuffer.setFont(scoresFont);
				titleBuffer.drawString("Click to start", (this.getWidth()-100)/2, (this.getHeight()-10)/2);
				titleBuffer.drawString("By: David Goldman",(this.getWidth()-160)/2,(int)((this.getHeight()-10)/2.5));
				titleBuffer.setFont(titleFont);
				titleBuffer.drawString("Pong", (int)((this.getWidth()-100)/1.85), this.getHeight()/4);
			}
			//Draws title image
			screen.drawImage(titleImage, 0,0,600,400,this);
		}
		else //If game has started, draw current frame
		{
			screen.drawImage(offImage, 0,0,600,400,this);
		}
		//this.getToolkit().sync();
	}
	private class KeyMovement implements KeyListener
	{
		//Represents the delta y - change in y
		//every time a key is pressed
		private final int mod = 4;

		@Override
		public void keyPressed(KeyEvent arg0) 
		{
			int keyCode = arg0.getKeyCode();
			switch (keyCode)
			{
			case KeyEvent.VK_UP:
				play2dy = 0-mod;
				break;
			case KeyEvent.VK_DOWN:
				play2dy = mod;
				break;
			case KeyEvent.VK_W:
				play1dy = 0-mod;
				break;
			case KeyEvent.VK_S:
				play1dy = mod;
				break;
			}
		}
		@Override
		public void keyReleased(KeyEvent arg0) 
		{
			//If the key released is a direction key
			//then set the delta y for that player to 0.
			int keyCode = arg0.getKeyCode();
			if (keyCode == KeyEvent.VK_UP || keyCode ==KeyEvent.VK_DOWN)
				play2dy = 0;
			else if (keyCode == KeyEvent.VK_W || keyCode ==KeyEvent.VK_S)
				play1dy = 0;
		}
		@Override
		public void keyTyped(KeyEvent arg0) 
		{
			//If game has started and p is pressed,
			//then pause/unpause game
			//If game has started and c is pressed
			//then turn on/off sounds
			if (gameStarted)
			{
				if (arg0.getKeyChar() == 'p' || arg0.getKeyChar() == 'P')
				{
					isPaused=!isPaused;
				}
				if (arg0.getKeyChar() == 'c' || arg0.getKeyChar() == 'c')
					sounds=!sounds;
			}
		}
	}
	private class MouseList implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (!gameStarted)
			{
				gameStarted = true;
				start = System.currentTimeMillis();
			}
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {return;}

		@Override
		public void mouseExited(MouseEvent arg0) {return;}

		@Override
		public void mousePressed(MouseEvent arg0) {
			if (!gameStarted)
			{
				gameStarted = true;
				start = System.currentTimeMillis();
			}

		}
		@Override
		public void mouseReleased(MouseEvent arg0) {return;}
	}

}
