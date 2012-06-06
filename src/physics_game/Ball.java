package physics_game;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;


public class Ball 
{
	private final Random generator = new Random();
	private final double modSpeed = .2;
	private final double startSpeed = 1;
	private double posDX, posDY;
	private Point center;
	private Point origCenter;
	private int radius;
	private double dx, dy;
	private double x, y;
	private int rectXCord, rectYCord, rectHeight;
	private int rectTopY, minY, maxY;
	private double speed;
	
	public Ball(Point center, int radius, int minY, int maxY)
	{
		this.center = center;
		origCenter = new Point(center.x,center.y);
		x = center.x; y = center.y;
		this.radius = radius;
		speed = startSpeed;
		calculateRandomDeltas();
		dx = posDX;
		dy = posDY;
		rectXCord = 0;
		rectYCord = 0;
		this.minY = minY;
		this.maxY = maxY;
	}
	public void update()
	{
		x = x+dx*speed;
		y = y+dy*speed;
		center.setLocation((int)x,(int)y);
	}
	public void reset()
	{
		center.setLocation(origCenter.x,origCenter.y);
		x = origCenter.x;
		y= origCenter.y;
		speed = startSpeed;
		calculateRandomDeltas();
		dx = posDX;
		dy = posDY;
	}
	/**
	 * 
	 * @param newSpeed Represents the new value of speed
	 * @return True if speed has been changed
	 */
	public boolean setSpeed(double newSpeed)
	{
		if (newSpeed > 1 && newSpeed <= 3)
		{
			this.speed = newSpeed;
			return true;
		}
		else
			return false;
		
	}
	public void increaseSpeed()
	{
		if (speed < 3)
			speed = speed+modSpeed;
		//System.out.println("Speed : " + speed);
	}
	private void calculateRandomDeltas()
	{
		posDY = (generator.nextDouble()*1.5)+1;
		posDX = (generator.nextDouble()*2.5)+1;
		if (posDY > posDX)
		{
			double temp= posDY;
			posDY = posDX;
			posDX = temp;
		}
		if ((int) posDY == (int) posDX)
			posDX++;
		posDX = Math.max(1.5,posDX);
		posDY = Math.max(1,posDY);
	}
	public void resetDxDy(boolean isRight)
	{
		if (isRight)
			dx = posDX;
		else
			dx = 0-posDX;
		if (generator.nextDouble() < .5)
			dy = posDY;
		else dy = 0-posDY;
	}
	public void flipVerticalDirection()
	{
		dy = dy * -1;
	}
	public void flipHorizontalDirection()
	{
		dx = dx*-1;
	}
	public void switchDirectionValues()
	{
		double temp = dx;
		dx = dy;
		dy = temp;
	}
	/**
	 * 
	 * @param point Point to be compared to circle
	 * @return True if the Point is on the circle
	 */
	public boolean contains(Point point)
	{
		return (point.distance(center) <= radius);
	}
	/**
	 * 
	 * @param x New center's x point ( >= radius)
	 * @param y New center's y point ( >= radius)
	 */
	public void setCenter(int x, int y)
	{
		this.center.setLocation(x, y);
	}
	public void setCenter(Point center)
	{
		this.center.setLocation(center);
	}
	public boolean collidesWithPadle(Rectangle object)
	{
		//If center is in rectangle, short circuit, return true
		rectHeight = object.height;
		rectTopY = object.y;
		if (object.contains(center.x, center.y))
			return true;
		int testX = center.x;
		int testY = center.y;
		if (testX < object.x) 
			testX = object.x;
		if (testX > object.x+object.width)
			testX = object.x+object.width;
		rectXCord = testX;
		if (testY < object.y)
			testY = object.y;
		if (testY>object.y+object.height)
			testY = object.y+object.height;
		rectYCord = testY;
		return contains(new Point (testX,testY));
	}
	/**
	 * 
	 * @param leftPaddleHit Signifies if the ball should
	 * now move left (decrease x) or right (increase x)
	 * 
	 * Splits the Rectangle into 9 parts
	 * and changes the delta values based 
	 * on what part it hit
	 */
	public void changeDeltasBasedOnRect(boolean leftPaddleHit)
	{
		int mod = (leftPaddleHit) ? 1 : -1;
		if (rectYCord <= rectTopY+rectHeight/9)
		{
			dx = mod; dy = -3;
		}
		else if(rectYCord <= rectTopY+rectHeight*2/9.0)
		{
			dx = mod; dy = -2;
		}
		else if (rectYCord <= rectTopY+rectHeight*3.0/9)
		{
			dx = mod; dy = -1;
		}
		else if (rectYCord<=rectTopY+rectHeight*4.0/9)
		{
			dx = 2*mod; dy = -.25;
		}
		else if (rectYCord <= rectTopY+rectHeight*5.0/9)
		{
			dx = 2*mod; dy = 0;
		}
		else if (rectYCord<=rectTopY+rectHeight*6.0/9)
		{
			dx = 2*mod; dy = .25;
		}
		else if(rectYCord<=rectTopY+rectHeight*7.0/9)
		{
			dx = mod; dy = 1;
		}
		else if (rectYCord <= rectTopY+rectHeight*8.0/9)
		{
			dx = mod; dy = 2;
		}
		else
		{
			dx = mod; dy = 3;
		}
	}
	public int getRectXCord()
	{
		return rectXCord;
	}
	public int getRectYCord()
	{
		return rectYCord;
	}
	/**
	 * 
	 * @param y	Value signaling a line to check collision
	 * @return True if the line has a point in the circle
	 */
	public boolean collidesWithLinesY()
	{
		//If the ball is moving out of vertical bounds
		//then flip the change in y 
		if (y <= minY && center.y-radius <= minY)
		{
			dy = Math.abs(dy);
			return true;
		}
		if (y>= maxY && center.y+radius>= maxY)
		{
			dy = -1*Math.abs(dy);
			return true;
		}
		return false;
		
	}
	public int getX()
	{
		return center.x;
	}
	public int getY()
	{
		return center.y;
	}
}
