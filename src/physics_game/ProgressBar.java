package physics_game;


public class ProgressBar {
	private ProgressBarOutline outline;
	private ProgressBarFill fill;
	private double amt;
	private double min,max;
	private boolean increasing; 
	
	public ProgressBar(Position pos){
		outline = new ProgressBarOutline(pos);
		fill = new ProgressBarFill(this,new Position(pos.getX() + 2, pos.getY()+2));
		min = 0;
		max = 76;
		increasing = true;
	}
	public void update(){
		final double step  = 1/10.0;
		
		amt += ((increasing) ? 1 : -1)*step;
		if (amt < min){
			amt = min;
			increasing = true;
		}
		if (amt > max){
			amt = max;
			increasing = false;
		}
	}
	public double getAmount(){
		return amt;
	}
	
	public ProgressBarOutline getOutline(){
		return outline;
	}
	public ProgressBarFill getFill(){
		return fill;
	}
	
}
