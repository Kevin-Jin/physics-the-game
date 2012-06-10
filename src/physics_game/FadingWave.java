package physics_game;

import java.util.List;

public class FadingWave  extends Wave implements Expirable{
	private final double MAX_TIME = .2f;
	
	private int id;
	private double timeAlive;
	
	public FadingWave(double rot, Position pos){
		this.pos = pos;
		this.rot =rot;
		//boundPoly = BoundingPolygon.transformBoundingPolygon(baseBoundPoly, this);
	}
	
	@Override
	public int getEntityId() {
		return id;
	}

	@Override
	public void setEntityId(int id) {
		this.id = id;
	}

	@Override
	public boolean isExpired() {
		return timeAlive > MAX_TIME;
	}
	public void recalculate(List<CollidableDrawable> others, double xMin, double yAcceleration, double yVelocityMin, double tDelta) {
		timeAlive += tDelta;
	}
	@Override
	public float getAlpha(){
		double percent = 1 - timeAlive / MAX_TIME;
		if (percent > 1)
			percent = 1;
		if (percent < 0)
			percent = 0;
		return (float)percent/2;
	}
	public Position getPosition(){
		return pos;
		
	}
	public void collision(CollisionInformation collisionInfo, List<CollidableDrawable> others) {
		
	}

	
}
